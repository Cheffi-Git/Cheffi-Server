package com.cheffi.review.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class ReviewTuples {

	private final List<ReviewTuple> reviewTupleList;
	private final boolean hasKey;

	private ReviewTuples(List<ReviewTuple> reviewTupleList) {
		this.reviewTupleList = reviewTupleList;
		this.hasKey = true;
	}

	private ReviewTuples() {
		this.reviewTupleList = null;
		this.hasKey = false;
	}

	public boolean hasKey() {
		return this.hasKey;
	}

	public static ReviewTuples of(List<ReviewTuple> tuples) {
		return new ReviewTuples(tuples);
	}

	public static ReviewTuples empty() {
		return new ReviewTuples(new ArrayList<>());
	}

	public static ReviewTuples noKey() {
		return new ReviewTuples();
	}

	public List<Long> toIdList() {
		if (!this.hasKey || reviewTupleList == null)
			throw new IllegalStateException("키가 없는 경우에는 사용할 수 없습니다.");
		return this.reviewTupleList.stream()
			.map(ReviewTuple::getReviewId)
			.toList();
	}
}
