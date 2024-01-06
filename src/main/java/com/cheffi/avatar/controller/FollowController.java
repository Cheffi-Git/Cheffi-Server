package com.cheffi.avatar.controller;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cheffi.avatar.dto.GetFollowRequest;
import com.cheffi.avatar.dto.response.AddFollowResponse;
import com.cheffi.avatar.dto.response.GetFollowResponse;
import com.cheffi.avatar.dto.response.RecommendFollowResponse;
import com.cheffi.avatar.dto.response.UnfollowResponse;
import com.cheffi.avatar.service.FollowService;
import com.cheffi.common.response.ApiCursorPageResponse;
import com.cheffi.common.response.ApiResponse;
import com.cheffi.oauth.model.UserPrincipal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/follows")
public class FollowController {

	private final FollowService followService;

	@Tag(name = "${swagger.tag.follow}")
	@Operation(summary = "아바타 팔로우 등록 API",
		description = "팔로우 추가 - 인증 필요",
		security = {@SecurityRequirement(name = "session-token")})
	@PreAuthorize("hasRole('USER')")
	@PostMapping
	public ApiResponse<AddFollowResponse> addFollow(
		@AuthenticationPrincipal UserPrincipal principal,
		@Positive Long avatarId) {
		return ApiResponse.success(followService.addFollow(principal.getAvatarId(), avatarId));
	}

	@Tag(name = "${swagger.tag.follow}")
	@Operation(summary = "아바타 팔로우 취소 API",
		description = "팔로우 취소 - 인증 필요",
		security = {@SecurityRequirement(name = "session-token")})
	@PreAuthorize("hasRole('USER')")
	@DeleteMapping
	public ApiResponse<UnfollowResponse> unfollow(
		@AuthenticationPrincipal UserPrincipal principal,
		@Positive Long avatarId) {

		return ApiResponse.success(followService.unfollow(principal.getAvatarId(), avatarId));
	}

	@Tag(name = "${swagger.tag.follow}")
	@Operation(summary = "해당 유저가 팔로우하는 유저 목록 조회 API",
		description = "팔로잉 조회",
		security = {@SecurityRequirement(name = "session-token")})
	@GetMapping("/{id}/following")
	public ApiCursorPageResponse<GetFollowResponse, Long> followingList(
		@Positive @PathVariable("id") Long followerId,
		@AuthenticationPrincipal UserPrincipal principal,
		@ParameterObject @Valid GetFollowRequest request
	) {
		return ApiCursorPageResponse.success(
			followService.getFollowingByCursor(request, followerId, principal.getAvatarId()));
	}

	@Tag(name = "${swagger.tag.follow}")
	@Operation(summary = "해당 유저를 팔로우하는 유저 목록 조회 API",
		description = "팔로워 조회",
		security = {@SecurityRequirement(name = "session-token")})
	@GetMapping("/{id}/follower")
	public ApiCursorPageResponse<GetFollowResponse, Long> followerList(
		@Positive @PathVariable("id") Long followingId,
		@AuthenticationPrincipal UserPrincipal principal,
		@ParameterObject @Valid GetFollowRequest request
	) {
		return ApiCursorPageResponse.success(
			followService.getFollowerByCursor(request, followingId, principal.getAvatarId()));
	}

	@Tag(name = "${swagger.tag.main}")
	@Operation(summary = "태그별 팔로우 추천 목록 조회 API",
		description = "태그별 팔로우 추천 조회 12개의 데이터만 제공합니다. "
			+ "메인 페이지용 API 입니다. - 인증 필요",
		security = {@SecurityRequirement(name = "session-token")})
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/recommend/tags")
	public ApiResponse<List<RecommendFollowResponse>> recommendFollowee(
		@AuthenticationPrincipal UserPrincipal principal,
		@RequestParam("tag") @Positive Long tagId) {
		return ApiResponse.success(followService.recommendFollowee(tagId, principal.getAvatarId()));
	}

}


