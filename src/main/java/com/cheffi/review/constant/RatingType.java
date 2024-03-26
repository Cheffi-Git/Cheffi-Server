package com.cheffi.review.constant;

import lombok.Getter;

@Getter
public enum RatingType {
	GOOD(5), AVERAGE(3), BAD(-1),
	NONE(0);

	RatingType(int score) {
		this.score = score;
	}

	private final int score;
}
