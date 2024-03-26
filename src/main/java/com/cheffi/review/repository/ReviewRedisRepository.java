package com.cheffi.review.repository;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.BusinessException;
import com.cheffi.common.dto.RedisZSetRequest;
import com.cheffi.review.dto.ReviewTuple;

@Repository
public class ReviewRedisRepository {

	private final ZSetOperations<String, Object> opsForZSet;
	private final RedisOperations<String, Object> operations;

	public ReviewRedisRepository(RedisTemplate<String, Object> redisTemplate) {
		this.opsForZSet = redisTemplate.opsForZSet();
		this.operations = opsForZSet.getOperations();
	}

	public List<ReviewTuple> getTypedTupleList(String key, RedisZSetRequest req) {
		var tuples = opsForZSet.reverseRangeWithScores(key, req.getStart(), req.getEnd());
		if (tuples == null)
			throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
		return tuples.stream().map(ReviewTuple::of).sorted().toList();
	}

	public void addZSet(String key, Set<ZSetOperations.TypedTuple<Object>> info, Duration duration) {
		opsForZSet.add(key, info);
		operations.expire(key, duration);
	}

	public boolean hasKey(String key) {
		Boolean hasKey = operations.hasKey(key);
		if (hasKey == null)
			throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
		return hasKey;
	}
}
