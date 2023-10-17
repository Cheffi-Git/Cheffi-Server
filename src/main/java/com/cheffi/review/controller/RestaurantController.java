package com.cheffi.review.controller;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cheffi.common.response.ApiResponse;
import com.cheffi.review.dto.RestaurantInfoDto;
import com.cheffi.review.service.RestaurantInfoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/restaurants")
public class RestaurantController {
	private final RestaurantInfoService restaurantInfoService;

	@Tag(name = "Restaurant")
	@Operation(summary = "게시글 등록 용 식당 검색 API",
		description = "게시글 등록 용 식당 검색 API 입니다. 최대 10개 까지의 검색 결과만 제공됩니다.")
	@Parameter(name = "name", description = "검색할 식당의 이름(쿼리 파라미터)", required = true)
	@Parameter(name = "province", description = "검색할 지역의 시/도 (쿼리 파라미터)")
	@Parameter(name = "city", description = "검색할 지역의 시/군/구 (쿼리 파라미터)")
	@GetMapping
	public ApiResponse<List<RestaurantInfoDto>> searchRestaurantsByName(
		@RequestParam @NotBlank String name, @RequestParam String city, @RequestParam String province) {
		return ApiResponse.success(restaurantInfoService.searchRestaurantByName(name, PageRequest.of(0, 10)));
	}

}
