package com.cheffi.review.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cheffi.review.dto.ReviewTypedTuple;
import com.cheffi.review.dto.request.AreaSearchRequest;
import com.cheffi.review.helper.ReviewRedisKeyGenerator;
import com.cheffi.review.helper.ReviewSearchPeriodStrategy;
import com.cheffi.review.repository.ReviewRedisRepository;
import com.cheffi.util.constant.SearchConstant;
import com.cheffi.util.model.ExactPeriod;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReviewTrendingService {

	private final ReviewRankingService reviewRankingService;
	private final ReviewRedisRepository reviewRedisRepository;
	private final ReviewSearchPeriodStrategy reviewSearchPeriodStrategy;
	private final ReviewRedisKeyGenerator reviewRedisKeyGenerator;

	public List<ReviewTypedTuple> getTrendingReviewTuple(AreaSearchRequest request) {
		LocalDateTime now = LocalDateTime.now();
		String key = reviewRedisKeyGenerator.getTrendingSearchKey(request.getAddress(), now);
		Long offset = request.getCursor();
		Integer size = request.getSize();

		List<ReviewTypedTuple> typedTupleSet = reviewRedisRepository.getTypedTupleList(key, size, offset);
		if (!typedTupleSet.isEmpty())
			return typedTupleSet;

		if (reviewRedisRepository.hasKey(key))
			return List.of();

		// 키가 없을 경우 새로 인기급등 맛집을 계산한다.
		ExactPeriod ep = reviewSearchPeriodStrategy.getTrendingSearchPeriod(now);
		var info = reviewRankingService.calculateRanking(request.getAddress(), ep.getStart(), ep.getEnd());
		if (info.isEmpty())
			return List.of();
		reviewRedisRepository.addZSet(key, info, SearchConstant.TRENDING_UPDATE_CYCLE);
		return reviewRedisRepository.getTypedTupleList(key, size, offset);
	}

	public List<ReviewTypedTuple> getTrendingReviewTupleTest(AreaSearchRequest request) {
		Integer offset = request.getCursor().intValue();
		Integer end = offset + request.getSize();
		ExactPeriod ep = reviewSearchPeriodStrategy.getTrendingSearchPeriod(LocalDateTime.now());
		var info = reviewRankingService.calculateRanking(request.getAddress(), ep.getStart(), ep.getEnd());
		if (info.isEmpty())
			return List.of();
		return info.stream().map(ReviewTypedTuple::of).sorted().toList().subList(offset, end);
	}
}
