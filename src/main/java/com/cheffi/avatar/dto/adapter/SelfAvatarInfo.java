package com.cheffi.avatar.dto.adapter;

import com.cheffi.avatar.domain.Avatar;
import com.cheffi.avatar.domain.ProfilePhoto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record SelfAvatarInfo(
	@Schema(description = "아바타 식별자 (아바타 = 유저 개념)")
	Long id,
	@Schema(description = "유저 닉네임")
	String nickname,
	@Schema(description = "유저 소개말")
	String introduction,
	@Schema(description = "쉐피 코인")
	int cheffiCoin,
	@Schema(description = "포인트")
	int point,
	@Schema(description = "팔로워 수")
	int follower,
	@Schema(description = "팔로잉 수")
	int following,
	@Schema(description = "게시물 수")
	int post,
	@Schema(description = "프로필 사진 URL")
	String photoUrl
) {
	public static SelfAvatarInfo of(Avatar avatar, ProfilePhoto photo) {
		return SelfAvatarInfo.builder()
			.id(avatar.getId())
			.nickname(avatar.getNickname())
			.introduction(avatar.getIntroduction())
			.cheffiCoin(avatar.getCheffiCoinCnt())
			.point(avatar.getPointCnt())
			.follower(avatar.getFollowerCnt())
			.following(avatar.getFollowingCnt())
			.post(avatar.getPostCnt())
			.photoUrl(photo != null ? photo.getUrl() : null)
			.build();
	}

	public static SelfAvatarInfo of(Avatar avatar) {
		return SelfAvatarInfo.builder()
			.id(avatar.getId())
			.nickname(avatar.getNickname())
			.introduction(avatar.getIntroduction())
			.cheffiCoin(avatar.getCheffiCoinCnt())
			.point(avatar.getPointCnt())
			.follower(avatar.getFollowerCnt())
			.following(avatar.getFollowingCnt())
			.post(avatar.getPostCnt())
			.build();
	}

}
