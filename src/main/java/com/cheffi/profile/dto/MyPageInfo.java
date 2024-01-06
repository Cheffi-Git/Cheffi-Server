package com.cheffi.profile.dto;

import java.util.List;

import com.cheffi.avatar.domain.Avatar;
import com.cheffi.avatar.domain.AvatarTag;
import com.cheffi.avatar.dto.common.TagDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MyPageInfo(
	@Schema(description = "ID", example = "1")
	Long id,
	@Schema(description = "닉네임", example = "동구밭에서캔감자")
	String nickname,
	@Schema(description = "자기소개", example = "동구밭 과수원길에서 태어난 감자입니다.")
	String introduction,
	@Schema(description = "프로필 사진 URL")
	String photoUrl,
	@Schema(description = "팔로워 수", example = "16")
	int followerCount,
	@Schema(description = "팔로잉 수", example = "24")
	int followingCount,
	@Schema(description = "게시물 수", example = "24")
	int post,
	@Schema(description = "쉐피 코인")
	int cheffiCoin,
	@Schema(description = "포인트")
	int point,
	List<TagDto> tags
) {

	public static MyPageInfo of(Avatar avatar) {
		return MyPageInfo.builder()
			.id(avatar.getId())
			.nickname(avatar.getNickname())
			.introduction(avatar.getIntroduction())
			.cheffiCoin(avatar.getCheffiCoinCnt())
			.point(avatar.getPointCnt())
			.followerCount(avatar.getFollowerCnt())
			.followingCount(avatar.getFollowingCnt())
			.post(avatar.getPostCnt())
			.photoUrl(avatar.getPhoto().getUrl())
			.tags(avatar.getAvatarTags().stream()
				.map(AvatarTag::getTag)
				.map(TagDto::of).toList())
			.build();
	}

}
