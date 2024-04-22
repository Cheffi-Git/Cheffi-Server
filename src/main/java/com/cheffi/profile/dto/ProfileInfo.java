package com.cheffi.profile.dto;

import java.util.List;

import com.cheffi.avatar.domain.Avatar;
import com.cheffi.avatar.dto.common.TagDto;
import com.cheffi.tag.domain.Tag;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.querydsl.core.annotations.QueryProjection;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProfileInfo {
	@Schema(description = "ID", example = "1", required = true)
	private final Long id;
	@Schema(description = "닉네임")
	private final String nickname;
	@Schema(description = "자기소개", example = "동구밭 과수원길에서 태어난 감자입니다.")
	private final String introduction;
	@Schema(description = "팔로워 수", example = "16")
	private final int followerCount;
	@Schema(description = "팔로잉 수", example = "24")
	private final int followingCount;
	@Schema(description = "게시물 수", example = "24")
	private final int post;
	@Schema(description = "쉐피 코인")
	private final int cheffiCoin;
	@Schema(description = "포인트")
	private final int point;
	@Schema(description = "프로필 사진 URL")
	private final String photoUrl;
	@Schema(description = "팔로우 여부", example = "false")
	private final Boolean following;
	@Schema(description = "차단 여부", example = "false")
	private final Boolean blocking;
	private final List<TagDto> tags;

	@QueryProjection
	public ProfileInfo(Avatar avatar, String photoUrl, Boolean following, Boolean blocking, List<Tag> tags) {
		this.id = avatar.getId();
		this.nickname = avatar.stringNickname();
		this.introduction = avatar.getIntroduction();
		this.followerCount = avatar.getFollowerCnt();
		this.followingCount = avatar.getFollowingCnt();
		this.post = avatar.getPostCnt();
		this.cheffiCoin = avatar.getCheffiCoinCnt();
		this.point = avatar.getPointCnt();
		this.photoUrl = photoUrl;
		this.following = following;
		this.blocking = blocking;
		this.tags = tags.stream().map(TagDto::of).toList();
	}
}
