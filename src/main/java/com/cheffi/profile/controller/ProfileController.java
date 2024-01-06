package com.cheffi.profile.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cheffi.common.response.ApiCursorPageResponse;
import com.cheffi.common.response.ApiResponse;
import com.cheffi.common.service.SecurityContextService;
import com.cheffi.oauth.model.UserPrincipal;
import com.cheffi.profile.dto.MyPageInfo;
import com.cheffi.profile.dto.ProfileInfo;
import com.cheffi.profile.service.ProfileService;
import com.cheffi.review.dto.ReviewInfoDto;
import com.cheffi.review.dto.request.GetMyPageReviewRequest;
import com.cheffi.review.service.ReviewSearchService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/profile")
public class ProfileController {

	private final SecurityContextService securityContextService;
	private final ReviewSearchService reviewSearchService;
	private final ProfileService profileService;

	@Tag(name = "${swagger.tag.profile-info}")
	@Operation(summary = "마이페이지 정보 조회 API",
		description = "마이페이지 정보 조회 - 인증 필요",
		security = {@SecurityRequirement(name = "session-token")})
	@PreAuthorize("hasRole('USER')")
	@GetMapping
	public ApiResponse<MyPageInfo> myPageInfo(
		@AuthenticationPrincipal UserPrincipal principal) {
		return ApiResponse.success(profileService.getMyPageInfo(principal.getAvatarId()));
	}

	@Tag(name = "${swagger.tag.profile-info}")
	@Operation(summary = "타인 프로필 정보 조회 API",
		description = "타인 프로필 정보 조회",
		security = {@SecurityRequirement(name = "session-token")})
	@GetMapping("/{id}")
	public ApiResponse<ProfileInfo> profileInfo(
		@Positive @PathVariable("id") Long ownerId,
		@AuthenticationPrincipal UserPrincipal principal) {
		if (securityContextService.hasUserAuthority(principal))
			return ApiResponse.success(profileService.getProfile(ownerId, principal.getAvatarId()));
		return ApiResponse.success(profileService.getProfile(ownerId, null));
	}

	@Tag(name = "${swagger.tag.profile-info}")
	@Operation(summary = "프로필 작성 게시물 조회 API",
		description = "프로필 작성 게시물 조회 - 북마크 여부는 표시되지 않습니다.",
		security = {@SecurityRequirement(name = "session-token")})
	@GetMapping("/{id}/reviews")
	public ApiCursorPageResponse<ReviewInfoDto, Long> profileReviewsByWriter(
		@Positive @PathVariable("id") Long writerId,
		@ParameterObject @Valid GetMyPageReviewRequest request,
		@AuthenticationPrincipal UserPrincipal principal) {
		if (securityContextService.hasUserAuthority(principal))
			return ApiCursorPageResponse.success(
				reviewSearchService.searchByWriter(request, writerId, principal.getAvatarId()));
		return ApiCursorPageResponse.success(reviewSearchService.searchByWriter(request, writerId, null));
	}

	@Tag(name = "${swagger.tag.profile-info}")
	@Operation(summary = "프로필 북마크 게시물 조회 API",
		description = "프로필 북마크 게시물 조회 - 북마크 여부는 표시되지 않습니다.",
		security = {@SecurityRequirement(name = "session-token")})
	@GetMapping("/{id}/bookmarks")
	public ApiCursorPageResponse<ReviewInfoDto, Long> profileReviewsByBookmarks(
		@Positive @PathVariable("id") Long ownerId,
		@ParameterObject @Valid GetMyPageReviewRequest request,
		@AuthenticationPrincipal UserPrincipal principal) {
		if (securityContextService.hasUserAuthority(principal))
			return ApiCursorPageResponse.success(
				reviewSearchService.searchByBookmarks(request, ownerId, principal.getAvatarId()));
		return ApiCursorPageResponse.success(reviewSearchService.searchByBookmarks(request, ownerId, null));
	}

	@Tag(name = "${swagger.tag.profile-info}")
	@Operation(summary = "프로필 구매 게시물 조회 API",
		description = "프로필 구매 게시물 조회 - 북마크 여부는 표시되지 않습니다.",
		security = {@SecurityRequirement(name = "session-token")})
	@GetMapping("/{id}/purchase")
	public ApiCursorPageResponse<ReviewInfoDto, Long> profileReviewsByPurchaser(
		@Positive @PathVariable("id") Long purchaserId,
		@ParameterObject @Valid GetMyPageReviewRequest request,
		@AuthenticationPrincipal UserPrincipal principal) {
		if (securityContextService.hasUserAuthority(principal))
			return ApiCursorPageResponse.success(
				reviewSearchService.searchByPurchaser(request, purchaserId, principal.getAvatarId()));
		return ApiCursorPageResponse.success(reviewSearchService.searchByPurchaser(request, purchaserId, null));
	}

}
