package com.cheffi.cs.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cheffi.common.response.ApiResponse;
import com.cheffi.cs.dto.PostReportRequest;
import com.cheffi.cs.service.ReportService;
import com.cheffi.oauth.model.UserPrincipal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/reports")
public class ReportController {

	private final ReportService reportService;

	@Tag(name = "${swagger.tag.review-detail}")
	@Tag(name = "${swagger.tag.report}")
	@Operation(summary = "신고 API - 인증 필수",
		description = "신고 API - 같은 사용자를 같은 이유로 중복 신고할 수 없습니다.",
		security = {@SecurityRequirement(name = "session-token")})
	@PreAuthorize("hasRole('USER')")
	@PostMapping
	public ApiResponse<Void> postReport(
		@RequestBody @Valid PostReportRequest request,
		@AuthenticationPrincipal UserPrincipal principal) {
		reportService.report(principal.getAvatarId(), request);
		return ApiResponse.success();
	}

}
