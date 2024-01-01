package com.cheffi.review.dto.request;

import com.cheffi.common.constant.DetailedAddress;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegisterRestaurantRequest {

	@Schema(description = "식당의 상가명칭", example = "샐러디 마포점")
	@NotBlank
	private String name;

	@Valid
	private DetailedAddress detailedAddress;
}
