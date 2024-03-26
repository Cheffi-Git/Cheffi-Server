package com.cheffi.review.dto;

import java.util.Objects;

import org.springframework.data.redis.core.ZSetOperations;

import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.BusinessException;

public class ReviewTuple implements ZSetOperations.TypedTuple<Object> {

	private final Long reviewId;
	private final Double score;

	@Override
	public Object getValue() {
		return this.reviewId;
	}

	@Override
	public Double getScore() {
		return this.score;
	}

	@Override
	public int compareTo(ZSetOperations.TypedTuple<Object> tuple) {
		if (tuple.getScore() == null)
			return -1;
		return Double.compare(tuple.getScore(), this.score);
	}

	public Long getReviewId() {
		return reviewId;
	}

	private ReviewTuple(Long reviewId, Double score) {
		this.reviewId = reviewId;
		this.score = score;
	}

	public static ReviewTuple of(Long reviewId, Double score) {
		return new ReviewTuple(reviewId, score);
	}

	public static ReviewTuple of(ZSetOperations.TypedTuple<Object> tuple) {
		if (tuple.getValue() instanceof Integer integer)
			return new ReviewTuple(integer.longValue(), tuple.getScore());
		throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof ReviewTuple that))
			return false;
		return reviewId.equals(that.reviewId) && score.equals(that.score);
	}

	@Override
	public int hashCode() {
		return Objects.hash(reviewId, score);
	}
}
