package com.cheffi.review.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cheffi.common.response.ApiResponse;
import com.cheffi.oauth.model.UserPrincipal;
import com.cheffi.review.dto.request.DeleteBookmarkRequest;
import com.cheffi.review.dto.request.PostBookmarkRequest;
import com.cheffi.review.service.BookmarkService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/bookmarks")
public class BookmarkController {

	private final BookmarkService bookmarkService;

	@Tag(name = "${swagger.tag.bookmark}")
	@Operation(summary = "북마크 추가 API - 인증 필수",
		description = "인증 필수, 북마크 추가",
		security = {@SecurityRequirement(name = "session-token")})
	@PreAuthorize("hasRole('USER')")
	@PostMapping
	public ApiResponse<Void> addBookmark(
		@RequestBody @Valid PostBookmarkRequest request,
		@AuthenticationPrincipal UserPrincipal principal) {
		bookmarkService.addBookmark(principal.getAvatarId(), request.getReviewId());
		return ApiResponse.success();
	}

	@Tag(name = "${swagger.tag.bookmark}")
	@Operation(summary = "북마크 취소 API - 인증 필수",
		description = "인증 필수, 북마크 취소",
		security = {@SecurityRequirement(name = "session-token")})
	@PreAuthorize("hasRole('USER')")
	@DeleteMapping
	public ApiResponse<Void> cancelBookmark(
		@RequestBody @Valid DeleteBookmarkRequest request,
		@AuthenticationPrincipal UserPrincipal principal) {
		bookmarkService.cancelBookmark(principal.getAvatarId(), request.getReviewId());
		return ApiResponse.success();
	}

}
