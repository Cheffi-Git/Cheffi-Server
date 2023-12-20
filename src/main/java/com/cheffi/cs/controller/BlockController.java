package com.cheffi.cs.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cheffi.common.response.ApiResponse;
import com.cheffi.cs.dto.PostBlockRequest;
import com.cheffi.cs.service.BlockService;
import com.cheffi.oauth.model.UserPrincipal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/blocks")
public class BlockController {

	private final BlockService blockService;

	@Tag(name = "CS")
	@Operation(summary = "차단 등록 API - 인증 필수",
		description = "차단 등록 API - 인증 필수, 차단시 대상과의 모든 팔로우 관계가 취소됩니다.",
		security = {@SecurityRequirement(name = "session-token")})
	@PreAuthorize("hasRole('USER')")
	@PostMapping
	public ApiResponse<Void> addBlock(
		@RequestBody @Valid PostBlockRequest request,
		@AuthenticationPrincipal UserPrincipal principal) {
		blockService.block(principal.getAvatarId(), request);
		return ApiResponse.success();
	}

}