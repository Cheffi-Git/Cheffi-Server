package com.cheffi.region.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cheffi.common.response.ApiResponse;
import com.cheffi.region.service.RegionDto;
import com.cheffi.region.service.RegionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/region")
@RequiredArgsConstructor
public class RegionController {

	private final RegionService regionService;

	@Tag(name = "RegionInfo")
	@Operation(summary = "시, 군/구 정보 조회 API, 세종시는 군/구대신 읍/면 까지 조회합니다.")
	@GetMapping
	public ApiResponse<List<RegionDto>> getAllRegionInfo() {
		return ApiResponse.success(regionService.getRegion());
	}


}
