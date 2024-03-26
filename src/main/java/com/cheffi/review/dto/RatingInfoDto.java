package com.cheffi.review.dto;

import com.cheffi.review.constant.RatingType;
import com.cheffi.review.domain.Rating;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RatingInfoDto {
	@Schema(description = "평가 타입", example = "GOOD")
	private final RatingType ratingType;
	@Schema(description = "평가 여부", example = "true")
	private final boolean rated;

	private RatingInfoDto(RatingType ratingType, boolean rated) {
		this.ratingType = ratingType;
		this.rated = rated;
	}

	public static RatingInfoDto of(Rating rating) {
		return new RatingInfoDto(rating.getRatingType(), true);
	}

	public static RatingInfoDto notRated() {
		return new RatingInfoDto(RatingType.NONE, false);
	}

}
