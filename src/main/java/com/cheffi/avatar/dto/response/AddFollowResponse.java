package com.cheffi.avatar.dto.response;

import com.cheffi.avatar.domain.Follow;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Builder;

@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AddFollowResponse(
	Long followerId,
	Long followeeId) {

	public static AddFollowResponse from (Follow follow) {

		return AddFollowResponse.builder()
			.followerId(follow.getSubject().getId())
			.followeeId(follow.getTarget().getId())
			.build();
	}
}
