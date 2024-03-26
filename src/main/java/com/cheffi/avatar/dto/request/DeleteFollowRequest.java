package com.cheffi.avatar.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;

public record DeleteFollowRequest(
	@Schema(description = "팔로우 취소할 유저의 ID")
	@Positive Long id
) {
}
