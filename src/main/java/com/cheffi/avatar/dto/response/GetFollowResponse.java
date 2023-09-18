package com.cheffi.avatar.dto.response;

import java.util.List;

import com.cheffi.avatar.domain.Avatar;
import com.cheffi.avatar.domain.Follow;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GetFollowResponse(
	Long avatarId,
	String nickname
) {
	public static GetFollowResponse of(Avatar avatar) {
		return new GetFollowResponse(avatar.getId(), avatar.getNickname());
	}

	public static List<GetFollowResponse> mock() {
		return List.of(new GetFollowResponse(2L, "구창모"),
			new GetFollowResponse(3L, "신동갑"),
			new GetFollowResponse(4L, "이준경"),
			new GetFollowResponse(5L, "임성빈"),
			new GetFollowResponse(6L, "강민호")
		);
	}

	public static List<GetFollowResponse> mapFollowsToResponses(List<Follow> follows) {

		return follows.stream()
			.map(GetFollowResponse::of)
			.toList();
	}

	public static GetFollowResponse of(Follow follow) {
		return new GetFollowResponse(follow.getId(), follow.getTarget().getNickname());
	}
}
