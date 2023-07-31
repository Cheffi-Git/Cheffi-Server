package com.cheffi.review;

import com.cheffi.common.dto.RatingInfoDto;
import com.cheffi.common.dto.RestaurantInfoDto;
import com.cheffi.common.dto.ReviewInfoDto;
import com.cheffi.common.dto.ReviewPhotoInfoDto;
import com.cheffi.review.dto.request.RegisterReviewRequest;
import com.cheffi.review.dto.response.GetRegionalReviewsResponseDto;
import com.cheffi.review.dto.response.GetReviewResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class ReviewService {


    public GetReviewResponseDto getReview(Long reviewId) {

        List<ReviewPhotoInfoDto> mockReviewPhotoes = new ArrayList<>();
        for (Long i = 1L; i < 3; i++) {
            mockReviewPhotoes.add(new ReviewPhotoInfoDto(i,
                    "www.photoUrl" + i));
        }
        List<RatingInfoDto> mockRatingInfoes = new ArrayList<>();
        mockRatingInfoes.add(new RatingInfoDto("GOOD", 30));
        mockRatingInfoes.add(new RatingInfoDto("Bad", -7));


        return GetReviewResponseDto.builder()
                .reviewInfo(ReviewInfoDto.builder()
                        .id(5L)
                        .title("리뷰 제목")
                        .text("리뷰 내용")
                        .bookmarked(true)
                        .build())
                .restaurant(RestaurantInfoDto.builder()
                        .id(1L)
                        .name("태초밥")
                        .build())
                .reviewphotos(mockReviewPhotoes)
                .ratings(mockRatingInfoes)
                .build();
    }


    public List<GetRegionalReviewsResponseDto> getRegionalReviews() {

        List<GetRegionalReviewsResponseDto> mockDtoes = new ArrayList<>();
        for (Long i = 1L; i < 11; i++) {

            mockDtoes.add(new GetRegionalReviewsResponseDto(ReviewInfoDto.builder()
                    .id(i)
                    .title("title" + i)
                    .bookmarked(i % 2 == 0)
                    .build()));
        }

        return mockDtoes;
    }


    public void registerReview(RegisterReviewRequest requestDto) {

    }

}
