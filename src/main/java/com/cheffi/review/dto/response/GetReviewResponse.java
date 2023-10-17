package com.cheffi.review.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.cheffi.review.constant.RatingType;
import com.cheffi.review.domain.Review;
import com.cheffi.review.dto.MenuDto;
import com.cheffi.review.dto.RestaurantInfoDto;
import com.cheffi.review.dto.ReviewPhotoInfoDto;
import com.cheffi.review.dto.ReviewViewerInfoDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetReviewResponse {

	@Schema(description = "리뷰 ID", example = "1")
	private Long id;
	@Schema(description = "리뷰 제목", example = "태초동에 생긴 맛집!!")
	private String title;
	@Schema(description = "리뷰의 적힌 본문 내용", example = "초밥 태초세트 추천해요")
	private String text;
	@Schema(description = "북마크 여부", example = "false")
	private boolean bookmarked;
	@Schema(description = "본인 평가 여부", example = "false")
	private boolean ratedByUser;
	@Schema(description = "본인 평가 타입", example = "GOOD")
	private RatingType ratingType;
	private LocalDateTime createdDate;
	private Long timeLeftToLock;
	@Schema(description = "태그 일치 개수", example = "3")
	private Long matchedTagNum;
	@Schema(description = "리뷰의 맛집 정보")
	private RestaurantInfoDto restaurant;
	@Schema(description = "리뷰 작성자 정보")
	private ReviewWriterInfoDto writer;
	@Schema(description = "리뷰의 평가 정보")
	private Map<RatingType, Integer> ratings;
	@Schema(description = "리뷰의 사진 정보")
	private List<ReviewPhotoInfoDto> photos;
	@Schema(description = "리뷰의 메뉴 정보")
	private List<MenuDto> menus;

	public static GetReviewResponse ofNotAuthenticated(Review review, ReviewWriterInfoDto reviewWriterInfoDto) {
		return GetReviewResponse.builder()
			.id(review.getId())
			.title(review.getTitle())
			.text(review.getText())
			.createdDate(review.getCreatedDate())
			.timeLeftToLock(review.getTimeLeftToLock())
			.restaurant(RestaurantInfoDto.of(review.getRestaurant()))
			.writer(reviewWriterInfoDto)
			.ratings(review.getRatingInfoMap())
			.photos(review.getPhotos().stream().map(ReviewPhotoInfoDto::of).toList())
			.menus(review.getMenus().stream().map(MenuDto::of).toList())
			.ratedByUser(false)
			.bookmarked(false)
			.build();
	}

	public static GetReviewResponse ofAuthenticated(Review review, ReviewViewerInfoDto reviewViewerInfoDto,
		ReviewWriterInfoDto reviewWriterInfoDto) {
		return GetReviewResponse.builder()
			.id(review.getId())
			.title(review.getTitle())
			.text(review.getText())
			.createdDate(review.getCreatedDate())
			.timeLeftToLock(review.getTimeLeftToLock())
			.matchedTagNum(reviewViewerInfoDto.getMatchedTagNum())
			.restaurant(RestaurantInfoDto.of(review.getRestaurant()))
			.writer(reviewWriterInfoDto)
			.ratings(review.getRatingInfoMap())
			.photos(review.getPhotos().stream().map(ReviewPhotoInfoDto::of).toList())
			.menus(review.getMenus().stream().map(MenuDto::of).toList())
			.ratedByUser(reviewViewerInfoDto.isRatedByUser())
			.ratingType(reviewViewerInfoDto.getRatingType())
			.bookmarked(reviewViewerInfoDto.isBookmarked())
			.build();
	}

}
