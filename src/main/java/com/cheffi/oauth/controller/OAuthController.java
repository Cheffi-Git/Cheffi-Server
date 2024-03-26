package com.cheffi.oauth.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cheffi.common.response.ApiResponse;
import com.cheffi.oauth.dto.request.OidcLoginRequest;
import com.cheffi.oauth.dto.response.OidcLoginResponse;
import com.cheffi.oauth.service.OAuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}")
public class OAuthController {

	private final OAuthService oauthService;

	@Tag(name = "${swagger.tag.sign-in}")
	@Tag(name = "${swagger.tag.sign-up}")
	@Operation(summary = "소셜 로그인 API - 인증 필요",
		description = "소셜 로그인 API - 소셜 로그인시 받은 ID 토큰으로 로그인을 요청하는 API입니다."
		+ " 로그인에 성공하면 \"Authorization\" 헤더로 세션 토큰이 발급됩니다.")
	@Parameter(name = "provider", description = "소셜 로그인 플랫폼", example = "kakao", required = true)
	@PostMapping("/oauth/login/{provider}")
	public ApiResponse<OidcLoginResponse> oidcLogin(@NotBlank @PathVariable String provider,
		@RequestBody OidcLoginRequest oidcLoginRequest) {
		return ApiResponse.success(oauthService.oidcLogin(oidcLoginRequest, provider));
	}

}
