package com.cheffi.avatar.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cheffi.avatar.dto.request.TagsChangeRequest;
import com.cheffi.avatar.dto.response.TagsChangeResponse;
import com.cheffi.avatar.service.AvatarTagService;
import com.cheffi.common.response.ApiResponse;
import com.cheffi.oauth.model.UserPrincipal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/avatars/tags")
public class AvatarTagController {

	private final AvatarTagService avatarTagService;

	@Tag(name = "${swagger.tag.sign-up}")
	@Tag(name = "${swagger.tag.profile-update}")
	@Operation(summary = "아바타 태그 변경",
		description = "자신의 태그 변경 - 인증 필요",
		security = {@SecurityRequirement(name = "session-token")})
	@PreAuthorize("hasRole('USER')")
	@PutMapping
	public ApiResponse<TagsChangeResponse> changeTags(
		@RequestBody TagsChangeRequest tagsChangeRequest,
		@AuthenticationPrincipal UserPrincipal principal) {
		return ApiResponse.success(avatarTagService.changeTags(principal.getAvatarId(), tagsChangeRequest));
	}

}
