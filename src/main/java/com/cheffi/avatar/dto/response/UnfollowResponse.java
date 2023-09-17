package com.cheffi.avatar.dto.response;

import com.cheffi.avatar.domain.Follow;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Builder;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UnfollowResponse(
	Long followerId,
	Long followeeId
) {
}
