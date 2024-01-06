package com.cheffi.review.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cheffi.common.dto.RedisZSetRequest;
import com.cheffi.review.domain.Review;
import com.cheffi.review.dto.ReviewSearchCondition;
import com.cheffi.review.dto.ReviewTuple;
import com.cheffi.review.dto.ReviewTuples;
import com.cheffi.review.helper.ReviewRedisKeyGenerator;
import com.cheffi.review.helper.ReviewSearchPeriodStrategy;
import com.cheffi.review.repository.ReviewRedisRepository;
import com.cheffi.util.model.ExactPeriod;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReviewTrendingService {
	private final ReviewService reviewService;
	private final ReviewSearchPeriodStrategy reviewSearchPeriodStrategy;
	private final ReviewRedisKeyGenerator reviewRedisKeyGenerator;
	private final ReviewRankingService reviewRankingService;
	private final ReviewRedisRepository reviewRedisRepository;

	public ReviewTuples getTrendingReviewTuples(ReviewSearchCondition condition, RedisZSetRequest request) {
		String key = reviewRedisKeyGenerator.getTrendingSearchKey(condition);
		List<ReviewTuple> tuples = reviewRedisRepository.getTypedTupleList(key, request);
		if (!tuples.isEmpty() || reviewRedisRepository.hasKey(key))
			return ReviewTuples.of(tuples);
		return ReviewTuples.noKey();
	}

	public ReviewTuples calculateTrendingReviews(ReviewSearchCondition condition, RedisZSetRequest request) {
		List<Review> reviews = reviewService.getByCondition(condition);
		String key = reviewRedisKeyGenerator.getTrendingSearchKey(condition);
		ExactPeriod ep = reviewSearchPeriodStrategy.getSearchPeriod(condition);

		var info = reviewRankingService.calculateRanking(reviews, ep.getStart(), ep.getEnd());
		if (info.isEmpty())
			return ReviewTuples.empty();
		reviewRedisRepository.addZSet(key, info, condition.getUnit().getDuration());
		return ReviewTuples.of(reviewRedisRepository.getTypedTupleList(key, request));
	}

}
