package com.cheffi.review.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class QueryRestaurantRequest {

	@Schema(description = "경도", example = "126.9960")
	@Nullable
	@Positive
	private Double x;

	@Schema(description = "위도", example = "37.5601")
	@Nullable
	@Positive
	private Double y;

	/**
	 * 37.504487, 127.048957 는 선릉역의 좌표
	 */
	public QueryRestaurantRequest(Double x, Double y) {
		this.x = x != null ? x : 127.048957;
		this.y = y != null ? y : 37.504487;
	}

}
