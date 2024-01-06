package com.cheffi.review.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.avatar.domain.Avatar;
import com.cheffi.avatar.service.PurchasedItemService;
import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.AuthenticationException;
import com.cheffi.common.config.exception.business.BusinessException;
import com.cheffi.common.dto.CursorPage;
import com.cheffi.common.dto.RedisZSetRequest;
import com.cheffi.region.service.RegionService;
import com.cheffi.review.domain.Review;
import com.cheffi.review.dto.AddressSearchRequest;
import com.cheffi.review.dto.MenuSearchRequest;
import com.cheffi.review.dto.ReviewCursor;
import com.cheffi.review.dto.ReviewInfoDto;
import com.cheffi.review.dto.ReviewSearchCondition;
import com.cheffi.review.dto.ReviewTuples;
import com.cheffi.review.dto.request.AreaSearchRequest;
import com.cheffi.review.dto.request.AreaTagSearchRequest;
import com.cheffi.review.dto.request.GetMyPageReviewRequest;
import com.cheffi.review.dto.response.GetReviewResponse;
import com.cheffi.review.dto.response.ReviewWriterInfoDto;
import com.cheffi.tag.constant.TagType;
import com.cheffi.tag.service.TagService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ReviewSearchService {

	private final ReviewService reviewService;
	private final RegionService regionService;
	private final PurchasedItemService purchasedItemService;
	private final ReviewAvatarService reviewAvatarService;
	private final ViewHistoryService viewHistoryService;
	private final ReviewTrendingService reviewTrendingService;
	private final TagService tagService;

	@Transactional
	public GetReviewResponse getReviewInfoOfNotAuthenticated(Long reviewId) {
		Review review = getReviewFromDB(reviewId);

		Avatar writer = review.getWriter();
		if (review.isLocked())
			throw new AuthenticationException(ErrorCode.ANONYMOUS_USER_CANNOT_ACCESS_LOCKED_REVIEW);

		viewHistoryService.readReviewAnonymous(reviewId);

		return GetReviewResponse.ofNotAuthenticated(review, ReviewWriterInfoDto.of(writer));
	}

	@Transactional
	public GetReviewResponse getReviewInfoOfAuthenticated(Long reviewId, Long viewerId) {
		Review review = getReviewFromDB(reviewId);
		Avatar writer = review.getWriter();

		if (!writer.hasSameIdWith(viewerId)) {
			if (review.isLocked() && !purchasedItemService.hasUnlocked(viewerId, reviewId)) {
				throw new BusinessException(ErrorCode.REVIEW_NOT_UNLOCKED);
			}
			viewHistoryService.readReview(viewerId, reviewId);
		}

		return GetReviewResponse.ofAuthenticated(review, reviewAvatarService.getInfoOfViewer(viewerId, reviewId),
			ReviewWriterInfoDto.of(writer, writer.getId().equals(viewerId)));
	}

	private Review getReviewFromDB(Long reviewId) {
		Review review = reviewService.getByIdWithEntities(reviewId);
		if (!review.isActive())
			throw new BusinessException(ErrorCode.REVIEW_IS_INACTIVE);
		return review;
	}

	public CursorPage<ReviewInfoDto, Integer> searchReviewsByArea(AreaSearchRequest request, Long viewerId) {
		ReviewTuples reviewTuples = getTrendingReviewTuples(request, request.toSearchCondition());
		return CursorPage.of(
			reviewService.getInfoById(reviewTuples.toIdList(), request.getCursor(), viewerId),
			request.getSize(),
			ReviewInfoDto::getNumber,
			request.getReferenceTime());
	}

	public CursorPage<ReviewInfoDto, Integer> searchReviewsByAreaAndTag(AreaTagSearchRequest request, Long viewerId) {
		tagService.verifyTag(request.getTagId(), TagType.FOOD);
		ReviewTuples reviewTuples = getTrendingReviewTuples(request, request.toSearchCondition());

		return CursorPage.of(
			reviewService.getInfoById(reviewTuples.toIdList(), request.getCursor(), viewerId),
			request.getSize(),
			ReviewInfoDto::getNumber,
			request.getReferenceTime());
	}

	public CursorPage<ReviewInfoDto, ReviewCursor> searchByMenu(MenuSearchRequest request, Long viewerId) {
		return CursorPage.of(
			reviewService.getByMenu(request, viewerId),
			request.getSize(),
			ReviewCursor::of
		);
	}

	public CursorPage<ReviewInfoDto, ReviewCursor> searchByAddress(AddressSearchRequest request, Long viewerId) {
		if (!regionService.contains(request.getAddress()))
			throw new BusinessException(ErrorCode.ADDRESS_NOT_EXIST);

		return CursorPage.of(
			reviewService.getByAddress(request, viewerId),
			request.getSize(),
			ReviewCursor::of
		);
	}

	private ReviewTuples getTrendingReviewTuples(RedisZSetRequest request, ReviewSearchCondition condition) {
		if (!regionService.contains(condition.getAddress()))
			throw new BusinessException(ErrorCode.ADDRESS_NOT_EXIST);

		ReviewTuples reviewTuples = reviewTrendingService.getTrendingReviewTuples(condition, request);
		if (reviewTuples.hasKey())
			return reviewTuples;

		return reviewTrendingService.calculateTrendingReviews(condition, request);
	}

	public CursorPage<ReviewInfoDto, Long> searchByWriter(GetMyPageReviewRequest request, Long writerId, Long viewerId) {
		return CursorPage.of(reviewService.getByWriter(request, writerId, viewerId),
			request.getSize(),
			ReviewInfoDto::getId);
	}

	public CursorPage<ReviewInfoDto, Long> searchByBookmarks(GetMyPageReviewRequest request, Long ownerId, Long viewerId) {
		return CursorPage.of(reviewService.getByBookmarks(request, ownerId, viewerId),
			request.getSize(),
			ReviewInfoDto::getCursor);
	}

	public CursorPage<ReviewInfoDto, Long> searchByPurchaser(GetMyPageReviewRequest request, Long purchaserId,
		Long viewerId) {
		return CursorPage.of(reviewService.getByPurchaser(request, purchaserId, viewerId),
			request.getSize(),
			ReviewInfoDto::getCursor);
	}
}
