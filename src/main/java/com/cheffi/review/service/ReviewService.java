package com.cheffi.review.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.BusinessException;
import com.cheffi.review.domain.Review;
import com.cheffi.review.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ReviewService {

	private final ReviewRepository reviewRepository;

	public Review getById(Long reviewId) {
		return reviewRepository.findById(reviewId)
			.orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_EXIST));
	}

}
