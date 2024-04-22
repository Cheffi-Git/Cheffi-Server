package com.cheffi.avatar.dto.adapter;

import com.cheffi.avatar.domain.Avatar;
import com.cheffi.avatar.domain.Nickname;
import com.cheffi.avatar.domain.ProfilePhoto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record SelfAvatarInfo(
	@Schema(description = "아바타 식별자 (아바타 = 유저 개념)", required = true)
	Long id,
	@Schema(description = "유저 닉네임", required = true)
	Nickname nickname,
	@Schema(description = "유저 소개말", required = true)
	String introduction,
	@Schema(description = "쉐피 코인", required = true)
	int cheffiCoin,
	@Schema(description = "포인트", required = true)
	int point,
	@Schema(description = "팔로워 수", required = true)
	int follower,
	@Schema(description = "팔로잉 수", required = true)
	int following,
	@Schema(description = "게시물 수", required = true)
	int post,
	@Schema(description = "프로필 사진 URL", required = true)
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
