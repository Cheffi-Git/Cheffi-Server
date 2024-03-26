package com.cheffi.review.dto.request;

import com.cheffi.review.constant.RatingType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class PutRatingRequest {

	@Schema(description = "리뷰의 ID", example = "1")
	@NotNull
	@Positive
	private final Long id;

	@Schema(description = "평가의 타입", example = "GOOD")
	@NotNull
	private final RatingType type;

	public PutRatingRequest(Long id, RatingType type) {
		this.id = id;
		this.type = type;
	}
}
