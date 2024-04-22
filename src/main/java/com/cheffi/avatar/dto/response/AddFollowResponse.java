package com.cheffi.avatar.dto.response;

import com.cheffi.avatar.domain.Follow;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.Builder;

@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AddFollowResponse(
	@Schema(required = true) Long followerId,
	@Schema(required = true) Long followeeId) {

	public static AddFollowResponse from (Follow follow) {

		return AddFollowResponse.builder()
			.followerId(follow.getSubject().getId())
			.followeeId(follow.getTarget().getId())
			.build();
	}
}
