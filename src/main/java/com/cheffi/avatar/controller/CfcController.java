package com.cheffi.avatar.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cheffi.avatar.dto.CheffiCoinHistoryDto;
import com.cheffi.avatar.dto.CheffiCoinHistoryRequest;
import com.cheffi.avatar.service.CheffiCoinService;
import com.cheffi.common.response.ApiCursorPageResponse;
import com.cheffi.oauth.model.UserPrincipal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/avatars/cfc")
public class CfcController {

	private final CheffiCoinService cheffiCoinService;

	@Tag(name = "${swagger.tag.cfc}")
	@Operation(summary = "CFC 내역 조회 API - 인증 필요",
		description = "CFC 내역 조회 - 인증 필요 (커서 페이징)",
		security = {@SecurityRequirement(name = "session-token")})
	@PreAuthorize("hasRole('USER')")
	@GetMapping
	public ApiCursorPageResponse<CheffiCoinHistoryDto, Long> getCheffiCoinHistory(
		@ParameterObject @Valid CheffiCoinHistoryRequest request,
		@AuthenticationPrincipal UserPrincipal principal) {
		return ApiCursorPageResponse.success(cheffiCoinService.getCheffiCoinHistory(principal.getAvatarId(), request));
	}

}
