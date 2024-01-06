package com.cheffi.review.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cheffi.common.response.ApiResponse;
import com.cheffi.oauth.model.UserPrincipal;
import com.cheffi.review.dto.request.PutRatingRequest;
import com.cheffi.review.service.RatingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/reviews/rate")
public class RatingController {

	private final RatingService ratingService;

	@Tag(name = "${swagger.tag.review-detail}")
	@Operation(summary = "리뷰 평가 API - 인증 필요",
		description = "리뷰에 대한 평가를 하는 API - 취소를 원한다면 'type = NONE' 지정해주면 취소됩니다.")
	@PreAuthorize("hasRole('USER')")
	@PutMapping
	public ApiResponse<Void> putRating(@RequestBody @Valid PutRatingRequest request,
		@AuthenticationPrincipal UserPrincipal principal) {
		ratingService.putRating(request, principal.getAvatarId());
		return ApiResponse.success(null);
	}

}
