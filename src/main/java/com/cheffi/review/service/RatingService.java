package com.cheffi.review.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.avatar.service.AvatarService;
import com.cheffi.review.domain.Rating;
import com.cheffi.review.dto.RatingInfoDto;
import com.cheffi.review.dto.ScoreDto;
import com.cheffi.review.dto.request.PutRatingRequest;
import com.cheffi.review.repository.RatingJpaRepository;
import com.cheffi.review.repository.RatingRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RatingService {

	private final RatingRepository ratingRepository;
	private final AvatarService avatarService;
	private final ReviewService reviewService;
	private final RatingJpaRepository ratingJpaRepository;

	public RatingInfoDto getRatingInfoOf(Long avatarId, Long reviewId) {
		return ratingRepository.findByAvatarAndReview(avatarId, reviewId)
			.map(RatingInfoDto::of)
			.orElseGet(RatingInfoDto::notRated);
	}

	public List<ScoreDto> getRatingScoreBetween(List<Long> ids, LocalDateTime start, LocalDateTime end) {
		return ratingJpaRepository.countBetween(ids, start, end);
	}

	@Transactional
	public Long putRating(PutRatingRequest request, Long avatarId) {
		Optional<Rating> rating = ratingRepository.findByAvatarAndReviewFetch(avatarId, request.getId());
		if (rating.isPresent()) {
			rating.get().changeType(request.getType());
			return rating.get().getId();
		}

		return ratingRepository.save(Rating.of(
			avatarService.getById(avatarId),
			reviewService.getById(request.getId()),
			request.getType())).getId();
	}
}
