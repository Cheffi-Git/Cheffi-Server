package com.cheffi.review.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.common.constant.DetailedAddress;
import com.cheffi.review.dto.RestaurantInfoDto;
import com.cheffi.review.dto.ReviewInfoDto;
import com.cheffi.review.dto.ReviewPhotoInfoDto;
import com.cheffi.review.dto.response.SearchReviewResponse;

@Service
@Transactional(readOnly = true)
public class ReviewSearchService {

	public SearchReviewResponse searchReview(Long reviewId) {

		List<ReviewPhotoInfoDto> mockReviewPhotoes = new ArrayList<>();
		mockReviewPhotoes.add(
			new ReviewPhotoInfoDto(1L, 5, "https://cdn.mindgil.com/news/photo/202109/72274_10092_4553.jpg"));
		mockReviewPhotoes.add(
			new ReviewPhotoInfoDto(2L, 2, "https://dimg.donga.com/wps/NEWS/IMAGE/2011/08/18/39608221.2.jpg"));
		mockReviewPhotoes.add(
			new ReviewPhotoInfoDto(3L, 4, "http://www.lampcook.com/wi_files/food_top100/top5/5_8.jpg"));
		mockReviewPhotoes.add(
			new ReviewPhotoInfoDto(4L, 1, "http://www.lampcook.com/wi_files/food_top100/top5/5_14.jpg"));
		mockReviewPhotoes.add(
			new ReviewPhotoInfoDto(5L, 3, "http://www.lampcook.com/wi_files/food_top100/top5/5_11.jpg"));
		mockReviewPhotoes.add(
			new ReviewPhotoInfoDto(6L, 6, "http://www.lampcook.com/wi_files/food_top100/top5/5_17.jpg"));

		return SearchReviewResponse.builder()
			.reviewInfo(ReviewInfoDto.builder()
				.id(5L)
				.title("리뷰 제목")
				.text("리뷰 내용")
				.ratingCnt(27)
				.bookmarked(true)
				.build())
			.restaurant(RestaurantInfoDto.builder()
				.id(1L)
				.name("크레이지카츠")
				.detailedAddress(
					DetailedAddress.of("서울시",
						"마포구",
						"포은로2나길 44, 2층",
						"합정동 391-5 2층")
				)
				.build())
			.reviewPhotos(mockReviewPhotoes)
			.ratings(new ArrayList<>())
			.build();
	}

	public List<ReviewInfoDto> searchReviewsByArea(String areaName) {

		Random random = new Random();
		List<ReviewInfoDto> mockDtos = new ArrayList<>();
		for (long i = 1L; i <= 200; i++) {

			mockDtos.add(ReviewInfoDto.builder()
				.id(i)
				.title("title(" + i + ")")
				.text("text(" + i + ")")
				.ratingCnt(random.nextInt(50) + 1)
				.bookmarked(i % 2 == 0)
				.build());
		}

		return mockDtos;
	}
}
