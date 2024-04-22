package com.cheffi.review.dto;

import com.cheffi.review.domain.Review;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;

@Getter
public class ReviewCursor {

	private final Long id;

	private final Integer count;

	@QueryProjection
	public ReviewCursor(Review review) {
		this.id = review.getId();
		this.count = review.getViewCnt();
	}

}
