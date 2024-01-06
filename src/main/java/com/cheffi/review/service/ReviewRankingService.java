package com.cheffi.review.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.review.domain.Review;
import com.cheffi.review.domain.ReviewRanking;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ReviewRankingService {
	private final RatingService ratingService;
	private final ViewHistoryService viewHistoryService;

	@Transactional(readOnly = true)
	public Set<ZSetOperations.TypedTuple<Object>> calculateRanking(List<Review> reviews, LocalDateTime from,
		LocalDateTime to) {
		if (reviews.isEmpty())
			return Set.of();
		ReviewRanking reviewRanking = new ReviewRanking(reviews);

		List<Long> ids = reviewRanking.getIds();
		reviewRanking.includeScore(ratingService.getRatingScoreBetween(ids, from, to));
		reviewRanking.includeScore(viewHistoryService.getViewScoreBetween(ids, from, to));

		return reviewRanking.toTuples();
	}
}
