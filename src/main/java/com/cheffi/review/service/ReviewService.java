package com.cheffi.review.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.BusinessException;
import com.cheffi.review.domain.Review;
import com.cheffi.review.dto.AddressSearchRequest;
import com.cheffi.review.dto.MenuSearchRequest;
import com.cheffi.review.dto.ReviewInfoDto;
import com.cheffi.review.dto.ReviewSearchCondition;
import com.cheffi.review.dto.request.GetMyPageReviewRequest;
import com.cheffi.review.repository.ReviewJpaRepository;
import com.cheffi.review.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final ReviewJpaRepository reviewJpaRepository;

	public Review getById(Long reviewId) {
		return reviewRepository.findById(reviewId)
			.orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_EXIST));
	}

	public List<ReviewInfoDto> getInfoById(List<Long> ids, Long offset, Long viewerId) {
		if (ids.isEmpty())
			return List.of();
		return updateNumber(ids, reviewJpaRepository.findAllById(ids, viewerId), offset);
	}

	private List<ReviewInfoDto> updateNumber(List<Long> ids, List<ReviewInfoDto> result, Long offset) {
		for (int i = 0; i < ids.size(); i++) {
			Long c = ids.get(i);
			int number = (int)(i + offset);
			result.stream().filter(r -> r.getId().equals(c))
				.forEach(r -> r.updateNumber(number));
		}
		result.sort(Comparator.comparingInt(ReviewInfoDto::getNumber));
		return result;
	}

	public Review getByIdWithEntities(Long reviewId) {
		return reviewRepository.findByIdWithEntities(reviewId)
			.orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_EXIST));
	}

	public List<Review> getByCondition(ReviewSearchCondition condition) {
		return reviewJpaRepository.findByCondition(condition);
	}

	public List<ReviewInfoDto> getByMenu(MenuSearchRequest request, Long viewerId) {
		return reviewJpaRepository.findByMenu(request, viewerId);
	}

	public Review save(Review review) {
		return reviewRepository.save(review);
	}

	public List<ReviewInfoDto> getByAddress(AddressSearchRequest request, Long viewerId) {
		return reviewJpaRepository.findByAddress(request, viewerId);
	}

	public List<ReviewInfoDto> getByWriter(GetMyPageReviewRequest request, Long writerId, Long viewerId) {
		return reviewJpaRepository.findByWriter(request, writerId, viewerId);
	}

	public List<ReviewInfoDto> getByBookmarks(GetMyPageReviewRequest request, Long ownerId, Long viewerId) {
		return reviewJpaRepository.findByBookmarks(request, ownerId, viewerId);
	}

	public List<ReviewInfoDto> getByPurchaser(GetMyPageReviewRequest request, Long purchaserId, Long viewerId) {
		return reviewJpaRepository.findByPurchaser(request, purchaserId, viewerId);
	}
}
