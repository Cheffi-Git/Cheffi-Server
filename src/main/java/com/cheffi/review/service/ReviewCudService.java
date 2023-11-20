package com.cheffi.review.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cheffi.avatar.domain.Avatar;
import com.cheffi.avatar.service.AvatarService;
import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.BusinessException;
import com.cheffi.common.config.exception.business.EntityNotFoundException;
import com.cheffi.review.domain.Restaurant;
import com.cheffi.review.domain.Review;
import com.cheffi.review.domain.ReviewCreateRequest;
import com.cheffi.review.dto.request.RegisterReviewRequest;
import com.cheffi.review.dto.request.UpdateReviewRequest;
import com.cheffi.review.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReviewCudService {

	private static final int LOCK_AFTER_HOURS = 24;

	private final ReviewRepository reviewRepository;
	private final AvatarService avatarService;
	private final RestaurantInfoService restaurantInfoService;
	private final ReviewTagService reviewTagService;
	private final MenuService menuService;
	private final ReviewPhotoService reviewPhotoService;

	@Transactional
	public Long registerReview(Long authorId, RegisterReviewRequest request, List<MultipartFile> images) {
		Avatar author = avatarService.getById(authorId);
		Restaurant restaurant = restaurantInfoService.getOrRegisterById(request.getRestaurantId(),
			request.isRegistered());
		Review review = Review.of(new ReviewCreateRequest(request.getTitle(), request.getText(), LOCK_AFTER_HOURS),
			restaurant, author);

		menuService.addMenus(review, request.getMenus());
		reviewTagService.changeTags(review, request.getTag());

		reviewPhotoService.addPhotos(review, images);

		return reviewRepository.save(review).getId();
	}

	@Transactional
	public Long updateReview(Long authorId, UpdateReviewRequest request, List<MultipartFile> images) {
		if (!avatarService.existById(authorId))
			throw new BusinessException(ErrorCode.AVATAR_NOT_EXISTS);
		Review review = reviewRepository.findById(request.getReviewId())
			.orElseThrow(() -> new EntityNotFoundException(ErrorCode.REVIEW_NOT_EXIST));
		if (review.getWriter().getId() != authorId) {
			throw new BusinessException(ErrorCode.NOT_REVIEW_WRITER);
		}

		menuService.changeMenus(review, request.getMenus());
		reviewTagService.changeTags(review, request.getTag());
		reviewPhotoService.changePhotos(review, images);

		return reviewRepository.save(review).getId();
	}

}
