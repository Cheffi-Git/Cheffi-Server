package com.cheffi.user.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cheffi.common.response.ApiResponse;
import com.cheffi.oauth.model.UserPrincipal;
import com.cheffi.user.dto.adapter.UserInfo;
import com.cheffi.user.dto.request.ChangeTermsAgreementRequest;
import com.cheffi.user.service.SignUpService;
import com.cheffi.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {

	private final UserService userService;
	private final SignUpService signUpService;

	@Tag(name = "User")
	@Operation(summary = "유저 정보 조회 API - 인증 필요",
		description = "자신의 계정 조회 - 인증 필요",
		security = {@SecurityRequirement(name = "session-token")})
	@GetMapping
	public ApiResponse<UserInfo> getSignedUserInfo(
		@AuthenticationPrincipal UserPrincipal principal) {
		return ApiResponse.success(userService.getUserInfo(principal.getUserId()));
	}

	@Tag(name = "${swagger.tag.sign-up}")
	@Tag(name = "${swagger.tag.profile-update}")
	@Operation(summary = "유저 약관 동의 변경 API - 인증 필요",
		description = "약관 동의 여부 변경 - 인증 필요",
		security = {@SecurityRequirement(name = "session-token")})
	@PatchMapping("/terms")
	public ApiResponse<UserInfo> changeTermsAgreement(
		@AuthenticationPrincipal UserPrincipal principal,
		@Valid @RequestBody ChangeTermsAgreementRequest request) {
		return ApiResponse.success(userService.changeTermsAgreement(principal.getUserId(),
			request.adAgreed(), false));
	}

	@Tag(name = "${swagger.tag.sign-up}")
	@Tag(name = "${swagger.tag.profile-update}")
	@Operation(summary = "유저 프로필 완료 등록 API - 인증 필요",
		description = "프로필 완료 등록 - 인증 필요 "
			+ "닉네임 설정, 프로필 사진 등록, 태그 설정이 끝난 후에 반드시 1회 호출하여 완료 등록을 해야 합니다.",
		security = {@SecurityRequirement(name = "session-token")})
	@PostMapping("/profile")
	public ApiResponse<List<String>> completeProfileRegistration(
		@AuthenticationPrincipal UserPrincipal principal) {
		return ApiResponse.success(
			signUpService.completeProfile(principal.getUserId(), principal.getAvatarId()).authorities());
	}

}
