package com.cheffi.avatar.dto.response;

import java.util.List;

import com.cheffi.avatar.domain.Avatar;
import com.cheffi.avatar.dto.common.TagDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Builder;

@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AvatarInfoResponse(
	Long id,
	String nickname,
	String pictureUrl,
	String introduction,
	int follower,
	int following,
	int post,
	List<TagDto> tagDtos
) {

	public static AvatarInfoResponse of(Avatar avatar) {
		return null;
	}

	public static AvatarInfoResponse mock() {
		return AvatarInfoResponse.builder()
			.id(1L)
			.nickname("닉네임")
			.pictureUrl("https://undongin.com/data/editor/0107/1609980770_6067.jpg")
			.introduction("안녕하세요. 소개글 입니다.")
			.follower(1700)
			.following(158)
			.post(18)
			.tagDtos(TagDto.mock())
			.build();
	}

}
