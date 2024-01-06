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

	@Tag(name = "${swagger.tag.notification}")
	@Operation(summary = "알림 목록 조회 API - 인증 필수",
		description = "알림 목록 조회 API - 인증 필수, 커서 페이징, 해당 API 로 조회된 알림은 자동으로 확인처리됩니다.",
		security = {@SecurityRequirement(name = "session-token")})
	@PreAuthorize("hasRole('USER')")
	@GetMapping
	public ApiCursorPageResponse<NotificationDto, Long> notifications(
		@ParameterObject @Valid GetNotificationRequest request,
		@AuthenticationPrincipal UserPrincipal principal) {
		return ApiCursorPageResponse.success(notificationService.getNotifications(request, principal.getAvatarId()));
	}

	@Tag(name = "${swagger.tag.notification}")
	@Operation(summary = "알림 삭제 API - 인증 필수",
		description = "알림 삭제 API - 인증 필수, 전체 삭제와 선택 삭제가 가능합니다.",
		security = {@SecurityRequirement(name = "session-token")})
	@PreAuthorize("hasRole('USER')")
	@DeleteMapping
	public ApiResponse<List<Long>> deleteNotifications(
		@RequestBody @Valid DeleteNotificationRequest request,
		@AuthenticationPrincipal UserPrincipal principal) {
		return ApiResponse.success(notificationService.deleteNotifications(request, principal.getAvatarId()));
	}

	@Tag(name = "${swagger.tag.main}")
	@Operation(summary = "알림 체크 API - 인증 필수",
		description = "알림 체크 API - 인증 필수, 확인하지 않은 알림이 있는지 체크하는 API입니다.",
		security = {@SecurityRequirement(name = "session-token")})
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/unchecked")
	public ApiResponse<Boolean> checkIfUncheckedExist(
		@AuthenticationPrincipal UserPrincipal principal) {
		return ApiResponse.success(notificationService.existUncheckedNotification(principal.getAvatarId()));
	}

}
