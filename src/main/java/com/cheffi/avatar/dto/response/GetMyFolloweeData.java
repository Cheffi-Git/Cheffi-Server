package com.cheffi.avatar.dto.response;

import java.util.List;

import com.cheffi.avatar.domain.Avatar;
import com.cheffi.avatar.domain.Follow;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GetMyFolloweeData(
	Long avatarId,
	String nickname,
	String photoUrl
) {
	public static GetMyFolloweeData of(Avatar avatar) {
		return new GetMyFolloweeData(avatar.getId(), avatar.getNickname(),avatar.getPhoto().getUrl());
	}

	public static List<GetMyFolloweeData> mock() {
		return List.of(new GetMyFolloweeData(2L, "구창모", "https://lh3.googleusercontent.com/p/AF1QipP679ZJyhDHfwdzUCJ44Oa6yguk2n0jv7dpVxOe=s1360-w1360-h1020"),
			new GetMyFolloweeData(3L, "신동갑", "https://lh3.googleusercontent.com/p/AF1QipP679ZJyhDHfwdzUCJ44Oa6yguk2n0jv7dpVxOe=s1360-w1360-h1020"),
			new GetMyFolloweeData(4L, "이준경", "https://lh3.googleusercontent.com/p/AF1QipP679ZJyhDHfwdzUCJ44Oa6yguk2n0jv7dpVxOe=s1360-w1360-h1020"),
			new GetMyFolloweeData(5L, "임성빈", "https://lh3.googleusercontent.com/p/AF1QipP679ZJyhDHfwdzUCJ44Oa6yguk2n0jv7dpVxOe=s1360-w1360-h1020"),
			new GetMyFolloweeData(6L, "강민호", "https://lh3.googleusercontent.com/p/AF1QipP679ZJyhDHfwdzUCJ44Oa6yguk2n0jv7dpVxOe=s1360-w1360-h1020")
		);
	}

	public static List<GetMyFolloweeData> mapFollowsToData(List<Follow> follows) {

		return follows.stream()
			.map(GetMyFolloweeData::of)
			.toList();
	}

	public static GetMyFolloweeData of(Follow follow) {
		return new GetMyFolloweeData(
			follow.getId(),
			follow.getTarget().getNickname(),
			follow.getTarget().getPhoto().getUrl());
	}
}
