package com.cheffi.review.dto;

import java.time.Duration;
import java.time.LocalDateTime;

import com.cheffi.review.constant.ReviewStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.querydsl.core.annotations.QueryProjection;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReviewInfoDto {

	@Schema(description = "리뷰 ID", example = "1")
	private Long id;
	@Schema(description = "리뷰 제목", example = "태초동에 생긴 맛집!!")
	private String title;
	@Schema(description = "리뷰의 적힌 본문 내용", example = "초밥 태초세트 추천해요")
	private String text;
	@Schema(description = "리뷰의 사진 URL")
	private ReviewPhotoInfoDto photo;
	@Schema(description = "잠금까지 남은 시간 (ms 단위)", example = "86399751")
	private Long timeLeftToLock;
	@Schema(description = "잠금 여부", example = "true")
	private Boolean locked;
	@Schema(description = "누적 조회수", example = "100")
	private Integer viewCount;
	@Schema(description = "랭킹(커서)", example = "10")
	private Integer number;
	@Schema(description = "리뷰의 현재 상태", example = "ACTIVE")
	private ReviewStatus reviewStatus;
	@Schema(description = "작성자 여부", example = "false")
	private Boolean writtenByUser;
	@Schema(description = "북마크 여부", example = "true")
	private Boolean bookmarked;
	@Schema(description = "구매 여부", example = "true")
	private Boolean purchased;
	@Schema(description = "리뷰의 활성화 여부 'ACTIVE' 상태이면 true", example = "true")
	private Boolean active;
	@JsonIgnore
	private Long cursor;

	@QueryProjection
	public ReviewInfoDto(Long id, String title, String text, ReviewPhotoInfoDto photo,
		LocalDateTime timeToLock, Integer viewCount, ReviewStatus reviewStatus, Boolean writtenByUser,
		Boolean bookmarked, Boolean purchased, Long cursor) {
		this.reviewStatus = reviewStatus;
		this.id = id;
		if (!ReviewStatus.ACTIVE.equals(reviewStatus))
			return;
		this.active = true;
		this.title = title;
		this.text = text;
		this.photo = photo;
		this.timeLeftToLock = Duration.between(LocalDateTime.now(), timeToLock).toMillis();
		this.locked = timeLeftToLock <= 0;
		this.bookmarked = bookmarked;
		this.viewCount = viewCount;
		this.writtenByUser = writtenByUser;
		this.purchased = purchased;
		this.cursor = cursor;
	}

	public void updateNumber(Integer number) {
		this.number = number;
	}
}
