package com.cheffi.review.dto;

import com.cheffi.review.domain.ReviewPhoto;
import com.querydsl.core.annotations.QueryProjection;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ReviewPhotoInfoDto {

	@Schema(description = "리뷰 사진ID", example = "1")
	private Long id;
	@Schema(description = "리뷰에 보여질 사진의 순서", example = "1")
	private int order;
	@Schema(description = "리뷰 사진의 URL", example = "https.www.~")
	private String photoUrl;

	@QueryProjection
	public ReviewPhotoInfoDto(Long id, int order, String photoUrl) {
		this.id = id;
		this.order = order;
		this.photoUrl = photoUrl;
	}

	public static ReviewPhotoInfoDto of(ReviewPhoto reviewPhoto) {
		return new ReviewPhotoInfoDto(reviewPhoto.getId(), reviewPhoto.getGivenOrder(), reviewPhoto.getUrl());
	}

}

