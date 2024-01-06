package com.cheffi.cs.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cheffi.common.response.ApiCursorPageResponse;
import com.cheffi.common.response.ApiResponse;
import com.cheffi.cs.dto.DeleteBlockRequest;
import com.cheffi.cs.dto.GetBlockRequest;
import com.cheffi.cs.dto.GetBlockResponse;
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

	@Tag(name = "${swagger.tag.block}")
	@Operation(summary = "차단 등록 API - 인증 필수",
		description = "차단 등록 API - 인증 필수, 차단시 대상과의 모든 팔로우 관계가 취소됩니다.",
		security = {@SecurityRequirement(name = "session-token")})
	@PreAuthorize("hasRole('USER')")
	@PostMapping
	public ApiResponse<Void> addBlock(
		@RequestBody @Valid PostBlockRequest request,
		@AuthenticationPrincipal UserPrincipal principal) {
		blockService.block(request, principal.getAvatarId());
		return ApiResponse.success();
	}

	@Tag(name = "${swagger.tag.block}")
	@Operation(summary = "차단 해제 API - 인증 필수",
		description = "차단 해제 API - 인증 필수, 차단이 해제됩니다. 차단 이전의 팔로우 관계는 복구되지 않습니다.",
		security = {@SecurityRequirement(name = "session-token")})
	@PreAuthorize("hasRole('USER')")
	@DeleteMapping
	public ApiResponse<Void> unblock(
		@RequestBody @Valid DeleteBlockRequest request,
		@AuthenticationPrincipal UserPrincipal principal) {
		blockService.unblock(request, principal.getAvatarId());
		return ApiResponse.success();
	}

	@Tag(name = "${swagger.tag.block}")
	@Operation(summary = "차단 목록 조회 API - 인증 필수",
		description = "차단 목록 조회 API - 인증 필수, 커서 페이징",
		security = {@SecurityRequirement(name = "session-token")})
	@PreAuthorize("hasRole('USER')")
	@GetMapping
	public ApiCursorPageResponse<GetBlockResponse, Long> blockList(
		@ParameterObject @Valid GetBlockRequest request,
		@AuthenticationPrincipal UserPrincipal principal) {
		return ApiCursorPageResponse.success(blockService.getBlockList(request, principal.getAvatarId()));
	}

}
