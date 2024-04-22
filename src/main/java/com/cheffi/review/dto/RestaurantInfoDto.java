package com.cheffi.review.dto;

import com.cheffi.common.constant.DetailedAddress;
import com.cheffi.review.domain.RestaurantInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
public class RestaurantInfoDto {

	@Schema(description = "식당 식별자", example = "3", required = true)
	private final Long id;
	@Schema(description = "식당 이름", example = "을밀대", required = true)
	private final String name;
	@Schema(description = "맛집 상세주소", required = true)
	private final DetailedAddress address;
	@Schema(description = "DB 등록 여부", required = true)
	private final boolean registered;

	@Builder
	private RestaurantInfoDto(Long id, String name, DetailedAddress detailedAddress, boolean registered) {
		this.id = id;
		this.name = name;
		this.address = detailedAddress;
		this.registered = registered;
	}

	public static RestaurantInfoDto of(RestaurantInfo restaurant) {
		return RestaurantInfoDto.builder()
			.id(restaurant.getId())
			.name(restaurant.getName())
			.detailedAddress(restaurant.getDetailedAddress())
			.registered(restaurant.isRegistered())
			.build();
	}

}
