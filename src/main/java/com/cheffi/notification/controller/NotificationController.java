package com.cheffi.notification.controller;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cheffi.common.response.ApiCursorPageResponse;
import com.cheffi.common.response.ApiResponse;
import com.cheffi.notification.dto.DeleteNotificationRequest;
import com.cheffi.notification.dto.GetNotificationRequest;
import com.cheffi.notification.dto.NotificationDto;
import com.cheffi.notification.service.NotificationService;
import com.cheffi.oauth.model.UserPrincipal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/notifications")
public class NotificationController {

	private final NotificationService notificationService;

	@Tag(name = "CS")
	@Operation(summary = "알림 목록 조회 API - 인증 필수",
		description = "알림 목록 조회 API - 인증 필수, 커서 페이징",
		security = {@SecurityRequirement(name = "session-token")})
	@PreAuthorize("hasRole('USER')")
	@GetMapping
	public ApiCursorPageResponse<NotificationDto, Long> notifications(
		@ParameterObject @Valid GetNotificationRequest request,
		@AuthenticationPrincipal UserPrincipal principal) {
		return ApiCursorPageResponse.success(notificationService.getNotifications(request, principal.getAvatarId()));
	}

	@Tag(name = "CS")
	@Operation(summary = "알림 삭제 API - 인증 필수",
		description = "알림 삭제 API - 인증 필수",
		security = {@SecurityRequirement(name = "session-token")})
	@PreAuthorize("hasRole('USER')")
	@DeleteMapping
	public ApiResponse<List<Long>> deleteNotifications(
		@RequestBody @Valid DeleteNotificationRequest request,
		@AuthenticationPrincipal UserPrincipal principal) {
		return ApiResponse.success(notificationService.deleteNotifications(request, principal.getAvatarId()));
	}


}
