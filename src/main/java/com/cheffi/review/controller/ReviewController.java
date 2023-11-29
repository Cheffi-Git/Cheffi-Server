package com.cheffi.review.controller;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cheffi.common.response.ApiCursorPageResponse;
import com.cheffi.common.response.ApiResponse;
import com.cheffi.common.service.SecurityContextService;
import com.cheffi.oauth.model.UserPrincipal;
import com.cheffi.review.dto.ReviewInfoDto;
import com.cheffi.review.dto.request.AreaSearchRequest;
import com.cheffi.review.dto.request.AreaTagSearchRequest;
import com.cheffi.review.dto.request.RegisterReviewRequest;
import com.cheffi.review.dto.response.GetReviewResponse;
import com.cheffi.review.service.ReviewCudService;
import com.cheffi.review.service.ReviewSearchService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/reviews")
public class ReviewController {

	private final ReviewCudService reviewCudService;
	private final ReviewSearchService reviewSearchService;
	private final SecurityContextService securityContextService;

	@Tag(name = "Review")
	@Operation(summary = "리뷰 단건 조회 API",
		description = "리뷰가 잠금된 경우 인증이 필요합니다. 인증이되었더라도 리뷰를 API로 구매해야 합니다.")
	@GetMapping
	public ApiResponse<GetReviewResponse> getReviewInfoAuthenticated(@AuthenticationPrincipal UserPrincipal principal,
		@RequestParam("id") Long reviewId) {
		if (securityContextService.hasUserAuthority(principal))
			return ApiResponse.success(
				reviewSearchService.getReviewInfoOfAuthenticated(reviewId, principal.getAvatarId()));
		return ApiResponse.success(reviewSearchService.getReviewInfoOfNotAuthenticated(reviewId));
	}

	@Tag(name = "Review")
	@Operation(summary = "지역별 맛집 조회 API",
		description = "1. 미 인증시 bookmarked 필드는 모두 false 입니다.")
	@GetMapping("/areas")
	public ApiCursorPageResponse<ReviewInfoDto, Integer> searchReviewsByArea(
		@ParameterObject @Valid AreaSearchRequest request,
		@AuthenticationPrincipal UserPrincipal principal) {
		if (securityContextService.hasUserAuthority(principal))
			return ApiCursorPageResponse.success(
				reviewSearchService.searchReviewsByArea(request, principal.getAvatarId()));
		return ApiCursorPageResponse.success(reviewSearchService.searchReviewsByArea(request));
	}

	@Tag(name = "Review")
	@Operation(summary = "쉐피들의 인정 맛집 조회 API",
		description = "1. 미 인증시 bookmarked 필드는 모두 false 입니다.")
	@GetMapping("/areas/tags")
	public ApiCursorPageResponse<ReviewInfoDto, Integer> searchReviewsByTagAndArea(
		@ParameterObject @Valid AreaTagSearchRequest request,
		@AuthenticationPrincipal UserPrincipal principal) {
		if (securityContextService.hasUserAuthority(principal))
			return ApiCursorPageResponse.success(reviewSearchService.searchReviewsByAreaAndTag(request,
				principal.getAvatarId()));
		return ApiCursorPageResponse.success(reviewSearchService.searchReviewsByAreaAndTag(request));
	}

	@Tag(name = "Review")
	@Operation(summary = "리뷰 등록 API - 인증 필요",
		description = "인증 필요, "
			+ "1. 사진의 순서는 클라이언트에서 보낸 순서대로 등록됩니다."
			+ "2. 태그는 각각 1개 2개 제한을 반드시 지켜야 합니다."
			+ "3. 프로필을 등록한 유저만 리뷰 등록이 가능합니다."
			+ "content-type : multipart/form-data 형태로 아래의 형식에 맞춰서 보내면 정상적으로 작동합니다.",
		security = {@SecurityRequirement(name = "session-token")})
	@PreAuthorize("hasRole('USER') and !hasAuthority('NO_PROFILE')")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ApiResponse<Long> registerReview(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@RequestPart("request") @Valid RegisterReviewRequest request,
		@Parameter(description = "작성할 리뷰의 사진 파일")
		@RequestPart("images") @Size(min = 3, max = 10) List<MultipartFile> images) {
		return ApiResponse.success(reviewCudService.registerReview(userPrincipal.getAvatarId(), request, images));
	}

}
