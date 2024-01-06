package com.cheffi.review.dto.response;

import com.cheffi.avatar.domain.Avatar;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReviewWriterInfoDto {
	@Schema(description = "리뷰 작성자 ID", example = "1")
	private final Long id;
	@Schema(description = "리뷰 작성자 닉네임", example = "닉네임1234")
	private final String nickname;
	@Schema(description = "리뷰 작성자 프로필 사진 URL")
	private final String photoUrl;
	@Schema(description = "리뷰 작성자 소개글", example = "안녕하세요. 리뷰 작성자입니다.")
	private final String introduction;
	@Schema(description = "조회자(유저)가 작성자인지 여부", example = "false")
	private boolean writtenByViewer;

	private ReviewWriterInfoDto(Long id, String nickname, String photoUrl, String introduction, boolean writtenByViewer) {
		this.id = id;
		this.nickname = nickname;
		this.photoUrl = photoUrl;
		this.introduction = introduction;
		this.writtenByViewer = writtenByViewer;
	}

	public static ReviewWriterInfoDto of(Avatar avatar, boolean writtenByViewer) {
		return new ReviewWriterInfoDto(avatar.getId(), avatar.getNickname(), avatar.getPhoto().getUrl(),
			avatar.getIntroduction(), writtenByViewer);
	}

	public static ReviewWriterInfoDto of(Avatar avatar) {
		return of(avatar, false);
	}
}
