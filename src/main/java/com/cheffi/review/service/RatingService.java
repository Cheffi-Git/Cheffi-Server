package com.cheffi.review.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.review.dto.RatingInfoDto;
import com.cheffi.review.repository.RatingRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RatingService {

	private final RatingRepository ratingRepository;

	public RatingInfoDto getRatingInfoOf(Long avatarId, Long reviewId) {
		return ratingRepository.findByAvatarAndReview(avatarId, reviewId)
			.map(RatingInfoDto::of)
			.orElseGet(RatingInfoDto::notRated);
	}
}
