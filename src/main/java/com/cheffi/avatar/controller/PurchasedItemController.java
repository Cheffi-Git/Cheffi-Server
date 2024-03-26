package com.cheffi.avatar.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cheffi.avatar.dto.request.PurchaseReviewRequest;
import com.cheffi.avatar.service.PurchasedItemInfoService;
import com.cheffi.common.response.ApiResponse;
import com.cheffi.oauth.model.UserPrincipal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/reviews")
public class PurchasedItemController {

	private final PurchasedItemInfoService purchasedItemInfoService;

	@Tag(name = "Review")
	@Operation(summary = "리뷰 구매 API - 인증 필요",
		description = "리뷰를 쉐피코인으로 구매하는 API 입니다. 이미 구매했다면 오류메세지를 보냅니다.",
		security = {@SecurityRequirement(name = "session-token")})
	@PreAuthorize("hasRole('USER')")
	@PostMapping("/purchase")
	public ApiResponse<Long> purchaseReview(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@RequestBody PurchaseReviewRequest request) {
		return ApiResponse.success(
			purchasedItemInfoService.purchaseReview(userPrincipal.getAvatarId(), request.getReviewId()));
	}

}
