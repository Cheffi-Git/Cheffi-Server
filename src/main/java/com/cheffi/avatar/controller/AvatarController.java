package com.cheffi.avatar.controller;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cheffi.avatar.dto.adapter.SelfAvatarInfo;
import com.cheffi.avatar.dto.request.PatchNicknameRequest;
import com.cheffi.avatar.dto.request.PhotoTabChangeRequest;
import com.cheffi.avatar.dto.response.AvatarInfoResponse;
import com.cheffi.avatar.service.AvatarService;
import com.cheffi.common.config.swagger.SwaggerBody;
import com.cheffi.common.response.ApiResponse;
import com.cheffi.oauth.model.UserPrincipal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/avatars")
public class AvatarController {

	private final AvatarService avatarService;

	@Tag(name = "Avatar")
	@Operation(summary = "자신의 아바타 조회 API - 인증 필요",
		description = "자신의 아바타 조회 - 인증 필요",
		security = {@SecurityRequirement(name = "session-token")})
	@PreAuthorize("hasRole('USER')")
	@GetMapping
	public ApiResponse<SelfAvatarInfo> getSelfAvatarInfo(
		@AuthenticationPrincipal UserPrincipal principal) {
		return ApiResponse.success(avatarService.getSelfAvatarInfo(principal.getAvatarId()));
	}

	@Tag(name = "${swagger.tag.sign-up}")
	@Tag(name = "${swagger.tag.profile-update}")
	@Operation(summary = "자신의 닉네임 변경 API - 인증 필요",
		description = "닉네임 변경 - 인증 필요",
		security = {@SecurityRequirement(name = "session-token")})
	@PreAuthorize("hasRole('USER')")
	@PatchMapping("/nickname")
	public ApiResponse<String> patchNickname(
		@Valid @RequestBody PatchNicknameRequest request,
		@AuthenticationPrincipal UserPrincipal principal) {
		String nickname = avatarService
			.updateNickname(principal.getAvatarId(), request.nickname()).nickname().getValue();
		return ApiResponse.success(nickname);
	}

	@Tag(name = "${swagger.tag.sign-up}")
	@Tag(name = "${swagger.tag.profile-update}")
	@Operation(summary = "프로필 사진, 자기소개 변경 API - 인증 필요",
		description = "프로필 사진, 자기소개 변경 - 인증 필요, "
					  + "request 부분을 application/json 으로 설정해서 요청을 보내주세요.",
		security = {@SecurityRequirement(name = "session-token")})
	@SwaggerBody(content = @Content(
		encoding = @Encoding(name = "request", contentType = MediaType.APPLICATION_JSON_VALUE)))
	@PreAuthorize("hasRole('USER')")
	@PostMapping(value = "/photo-tab", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ApiResponse<String> changePhotoTab(
		@AuthenticationPrincipal UserPrincipal principal,
		@Parameter(description = "변경할 프로필 사진 파일, 자기소개")
		@RequestPart(value = "file", required = false) @Nullable MultipartFile file,
		@Valid @RequestPart("request") PhotoTabChangeRequest request
	) {
		return ApiResponse.success(
			avatarService.changePhotoTab(
				principal.getAvatarId(),
				request.introduction(),
				file,
				request.defaultPhoto()
			)
		);
	}

	@Tag(name = "${swagger.tag.sign-up}")
	@Tag(name = "${swagger.tag.profile-update}")
	@Operation(summary = "닉네임 중복 확인 API")
	@GetMapping(value = "/nickname/inuse")
	public ApiResponse<Boolean> checkNicknameIsInUse(@NotBlank String nickname) {
		return ApiResponse.success(avatarService.isNicknameInUse(nickname));
	}

	@Tag(name = "Avatar")
	@Operation(summary = "타인의 아바타 조회 API")
	@GetMapping("/{id}")
	public ApiResponse<AvatarInfoResponse> getAvatarInfo(@PathVariable(name = "id") @Positive Long avatarId) {
		return ApiResponse.success(avatarService.getAvatarInfo(avatarId));
	}

}
