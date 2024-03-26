package com.cheffi.review.controller;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cheffi.common.response.ApiResponse;
import com.cheffi.review.dto.RestaurantInfoDto;
import com.cheffi.review.dto.request.QueryRestaurantRequest;
import com.cheffi.review.dto.request.RegisterRestaurantRequest;
import com.cheffi.review.service.RestaurantInfoService;
import com.cheffi.review.service.RestaurantService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/restaurants")
public class RestaurantController {
	private final RestaurantInfoService restaurantInfoService;
	private final RestaurantService restaurantService;

	@Tag(name = "${swagger.tag.review-cud}")
	@Operation(summary = "게시글 등록용 식당 검색 API",
		description = "게시글 등록용 식당 검색 API 입니다. 최대 10개 까지의 검색 결과만 제공됩니다.")
	@Parameter(name = "name", description = "검색할 식당의 이름(쿼리 파라미터)", required = true)
	@Parameter(name = "province", description = "검색할 지역의 시/도 (쿼리 파라미터) - 아직 식당의 지역검색은 지원되지 않습니다.")
	@Parameter(name = "city", description = "검색할 지역의 시/군/구 (쿼리 파라미터) - 아직 식당의 지역검색은 지원되지 않습니다.")
	@GetMapping
	public ApiResponse<List<RestaurantInfoDto>> searchRestaurantsByName(
		@RequestParam @NotBlank String name) {
		return ApiResponse.success(restaurantInfoService.searchRestaurantByName(name, PageRequest.of(0, 10)));
	}

	@Tag(name = "${swagger.tag.review-cud}")
	@Operation(summary = "식당 등록 신청 API - 인증 필수",
		description = "사용자에 의한 식당 등록 API입니다. "
					  + "임시적으로 식당이 등록되며 관리자에 의해 최종 등록됩니다.")
	@PreAuthorize("hasRole('USER') and !hasAuthority('NO_PROFILE')")
	@PostMapping
	public ApiResponse<Long> registerTempRestaurant(@Valid @RequestBody RegisterRestaurantRequest request) {
		return ApiResponse.success(restaurantService.registerTempRestaurant(request));
	}

	@Tag(name = "${swagger.tag.review-cud}")
	@Operation(summary = "반경 5KM 이내 식당 조회 API - 인증 필수",
		description = "반경 5KM 이내 식당을 조회하는 API입니다. 최대 8개 까지의 검색 결과만 제공됩니다."
					  + "좌표가 입력되지 않을 경우 선릉역을 기준으로 조회합니다.")
	@PreAuthorize("hasRole('USER') and !hasAuthority('NO_PROFILE')")
	@GetMapping("/near")
	public ApiResponse<List<RestaurantInfoDto>> popularRestaurantsWithin(
		@Valid @ParameterObject QueryRestaurantRequest request) {
		return ApiResponse.success(restaurantInfoService.getPopularRestaurantsWithin(request));
	}

}
