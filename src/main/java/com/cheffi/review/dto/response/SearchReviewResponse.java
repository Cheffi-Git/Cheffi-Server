package com.cheffi.review.dto.response;

import java.util.List;

import com.cheffi.review.dto.RatingInfoDto;
import com.cheffi.review.dto.RestaurantInfoDto;
import com.cheffi.review.dto.ReviewInfoDto;
import com.cheffi.review.dto.ReviewPhotoInfoDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SearchReviewResponse {

	@Schema(description = "리뷰 정보", example = "너무 맛있어요")
	private ReviewInfoDto review;
	@Schema(description = "리뷰의 맛집 정보")
	private RestaurantInfoDto restaurant;

	@Schema(description = "리뷰의 맛집 정보")
	private List<ReviewPhotoInfoDto> reviewPhotos;
	@Schema(description = "리뷰의 타입별 총합점수")
	private List<RatingInfoDto> ratings;

	@Builder
	private SearchReviewResponse(ReviewInfoDto reviewInfo,
		RestaurantInfoDto restaurant,
		List<ReviewPhotoInfoDto> reviewPhotos,
		List<RatingInfoDto> ratings) {

		this.review = reviewInfo;
		this.restaurant = restaurant;
		this.reviewPhotos = reviewPhotos;
		this.ratings = ratings;
	}

}
