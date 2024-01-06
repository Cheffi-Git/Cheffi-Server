package com.cheffi.review.service;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cheffi.avatar.domain.Avatar;
import com.cheffi.avatar.service.AvatarService;
import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.BusinessException;
import com.cheffi.common.constant.S3RootPath;
import com.cheffi.common.service.SecurityContextService;
import com.cheffi.event.event.ReviewCreateEvent;
import com.cheffi.review.domain.Restaurant;
import com.cheffi.review.domain.Review;
import com.cheffi.review.domain.ReviewCreateRequest;
import com.cheffi.review.dto.request.DeleteReviewRequest;
import com.cheffi.review.dto.request.RegisterReviewRequest;
import com.cheffi.review.dto.request.UpdateReviewRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReviewCudService {

	private static final int LOCK_AFTER_HOURS = 24;

	private final AvatarService avatarService;
	private final RestaurantInfoService restaurantInfoService;
	private final ReviewTagService reviewTagService;
	private final MenuService menuService;
	private final ReviewPhotoService reviewPhotoService;
	private final ReviewService reviewService;
	private final SecurityContextService securityContextService;
	private final ApplicationEventPublisher eventPublisher;

	@Transactional
	public Long registerReview(Long writerId, RegisterReviewRequest request, List<MultipartFile> images) {
		Avatar writer = avatarService.getById(writerId);
		Restaurant restaurant = restaurantInfoService.getOrRegisterById(request.getRestaurantId(),
			request.isRegistered());
		Review review = Review.of(new ReviewCreateRequest(request.getTitle(), request.getText(), LOCK_AFTER_HOURS),
			restaurant, writer);
		writer.addPostCount();

		menuService.addMenus(review, request.getMenus());
		reviewTagService.changeTags(review, request.getTag());

		reviewPhotoService.addPhotos(review, images);

		Review savedReview = reviewService.save(review);
		eventPublisher.publishEvent(
			new ReviewCreateEvent(writer, savedReview, securityContextService.getAuthorities()));
		return savedReview.getId();
	}

	@Transactional
	public void updateReview(Long authorId, UpdateReviewRequest request, List<MultipartFile> images) {
		Avatar author = avatarService.getById(authorId);
		Review review = reviewService.getById(request.getId());
		validateReviewAuthor(author, review);

		review.updateFromRequest(request);
		menuService.changeMenus(review, request.getMenus());
		reviewTagService.changeTags(review, request.getTag());
		reviewPhotoService.changePhotos(review, images, S3RootPath.REVIEW_PHOTO);
	}

	@Transactional
	public void deleteReview(Long authorId, DeleteReviewRequest request) {
		Avatar author = avatarService.getById(authorId);
		Review review = reviewService.getById(request.id());
		validateReviewAuthor(author, review);
		review.delete();
	}

	private static void validateReviewAuthor(Avatar author, Review review) {
		if (review.getWriter().getId().equals(author.getId()))
			throw new BusinessException(ErrorCode.NOT_REVIEW_WRITER);
	}

}
