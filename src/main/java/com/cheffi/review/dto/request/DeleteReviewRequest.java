package com.cheffi.review.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record DeleteReviewRequest(
	@NotNull
	@Schema(description = "삭제할 리뷰 식별자", example = "1")
	Long id
) {
}
