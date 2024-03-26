package com.cheffi.avatar.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UnfollowResponse(
	@Schema(required = true) Long followerId,
	@Schema(required = true) Long followeeId
) {
}
