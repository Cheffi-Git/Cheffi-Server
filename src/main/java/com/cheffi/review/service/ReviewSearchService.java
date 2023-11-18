package com.cheffi.review.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.avatar.domain.Avatar;
import com.cheffi.avatar.service.PurchasedItemService;
import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.AuthenticationException;
import com.cheffi.common.config.exception.business.BusinessException;
import com.cheffi.common.dto.CursorPage;
import com.cheffi.region.service.RegionService;
import com.cheffi.review.domain.Review;
import com.cheffi.review.dto.ReviewInfoDto;
import com.cheffi.review.dto.ReviewTypedTuple;
import com.cheffi.review.dto.request.AreaSearchRequest;
import com.cheffi.review.dto.response.GetReviewResponse;
import com.cheffi.review.dto.response.ReviewWriterInfoDto;

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

	@Transactional
	public GetReviewResponse getReviewInfoOfNotAuthenticated(Long reviewId) {
		Review review = reviewService.getByIdWithEntities(reviewId);
		Avatar writer = review.getWriter();
		if (review.isLocked())
			throw new AuthenticationException(ErrorCode.ANONYMOUS_USER_CANNOT_ACCESS_LOCKED_REVIEW);

		viewHistoryService.readReviewAnonymous(reviewId);

		return GetReviewResponse.ofNotAuthenticated(review, ReviewWriterInfoDto.of(writer));
	}

	@Transactional
	public GetReviewResponse getReviewInfoOfAuthenticated(Long reviewId, Long viewerId) {
		Review review = reviewService.getByIdWithEntities(reviewId);
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

	public CursorPage<ReviewInfoDto, Integer> searchReviewsByArea(AreaSearchRequest request, Long viewerId) {
		if (!regionService.contains(request.getAddress()))
			throw new BusinessException(ErrorCode.ADDRESS_NOT_EXIST);
		List<ReviewInfoDto> reviewDtos = reviewService.getAllByIdWithBookmark(getTrendingReviewIndex(request),
			viewerId, request.getCursor());
		return CursorPage.of(reviewDtos, request.getSize(), ReviewInfoDto::getNumber);
	}

	public CursorPage<ReviewInfoDto, Integer> searchReviewsByArea(AreaSearchRequest request) {
		if (!regionService.contains(request.getAddress()))
			throw new BusinessException(ErrorCode.ADDRESS_NOT_EXIST);
		List<ReviewInfoDto> reviewDtos = reviewService.getAllById(getTrendingReviewIndex(request), request.getCursor());
		return CursorPage.of(reviewDtos, request.getSize(), ReviewInfoDto::getNumber);
	}

	private List<Long> getTrendingReviewIndex(AreaSearchRequest request) {
		return reviewTrendingService.getTrendingReviewTuple(request)
			.stream()
			.map(ReviewTypedTuple::getReviewId)
			.toList();
	}

	public CursorPage<ReviewInfoDto, Integer> searchReviewsByAreaTest(AreaSearchRequest request) {
		if (!regionService.contains(request.getAddress()))
			throw new BusinessException(ErrorCode.ADDRESS_NOT_EXIST);
		List<ReviewInfoDto> reviewDtos = reviewService.getAllById(getTrendingReviewIndexTest(request),
			request.getCursor());
		return CursorPage.of(reviewDtos, request.getSize(), ReviewInfoDto::getNumber);
	}

	private List<Long> getTrendingReviewIndexTest(AreaSearchRequest request) {
		return reviewTrendingService.getTrendingReviewTupleTest(request)
			.stream()
			.map(ReviewTypedTuple::getReviewId)
			.toList();
	}

}

