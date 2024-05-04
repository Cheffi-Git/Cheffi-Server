package com.cheffi.backoffice.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cheffi.backoffice.service.AdminRestaurantService;
import com.cheffi.common.response.ApiPageResponse;
import com.cheffi.review.domain.Restaurant;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/backoffice/restaurants")
public class AdminRestaurantController {

	private final AdminRestaurantService adminRestaurantService;

	@GetMapping
	public ApiPageResponse<Restaurant> searchRestaurants(
		@Parameter(hidden = true)
		@PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
		@RequestParam(required = false) String name
	) {
		return ApiPageResponse.success(adminRestaurantService.findAllRes(pageable, name));
	}
}
