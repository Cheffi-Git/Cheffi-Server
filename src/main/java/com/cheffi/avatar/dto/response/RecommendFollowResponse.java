package com.cheffi.avatar.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.querydsl.core.annotations.QueryProjection;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record RecommendFollowResponse(
	@Schema(description = "ID", example = "1")
	Long id,
	@Schema(description = "닉네임", example = "동구밭에서캔감자")
	String nickname,
	@Schema(description = "프로필 사진 URL")
	String photoUrl,
	@Schema(description = "자기소개", example = "동구밭 과수원길에서 태어난 감자입니다.")
	String introduction,
	@Schema(description = "팔로워 수", example = "16")
	int followers,
	@Schema(description = "팔로우 여부", example = "false")
	boolean followed
) {

	@QueryProjection
	public RecommendFollowResponse(Long id, String nickname, String photoUrl, String introduction, int followers,
		boolean followed) {
		this.id = id;
		this.nickname = nickname;
		this.photoUrl = photoUrl;
		this.introduction = introduction;
		this.followers = followers;
		this.followed = followed;
	}
}
