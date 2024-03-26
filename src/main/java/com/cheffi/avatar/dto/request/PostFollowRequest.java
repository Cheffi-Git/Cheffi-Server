package com.cheffi.avatar.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;

public record PostFollowRequest(
	@Schema(description = "팔로우할 유저의 ID")
	@Positive Long id
) {
}
