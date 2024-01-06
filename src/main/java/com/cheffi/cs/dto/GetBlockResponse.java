package com.cheffi.cs.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.querydsl.core.annotations.QueryProjection;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GetBlockResponse(
	@JsonIgnore
	Long cursor,
	@Schema(description = "아바타 ID", example = "1")
	Long id,
	@Schema(description = "아바타 닉네임", example = "고구마맛탕")
	String nickname,
	@Schema(description = "아바타 프로필 사진 URL")
	String photoUrl,
	LocalDateTime blockedDate
) {

	@QueryProjection
	public GetBlockResponse(
		Long cursor, Long id, String nickname, String photoUrl, LocalDateTime blockedDate) {
		this.cursor = cursor;
		this.id = id;
		this.nickname = nickname;
		this.photoUrl = photoUrl;
		this.blockedDate = blockedDate;
	}
}
