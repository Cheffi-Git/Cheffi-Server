package com.cheffi.review.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cheffi.review.constant.RatingType;
import com.cheffi.review.domain.Rating;
import com.cheffi.review.dto.RatingInfoDto;
import com.cheffi.review.repository.RatingRepository;

@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

	@Mock
	private RatingRepository ratingRepository;

	@InjectMocks
	private RatingService ratingService;

	@Mock
	private Rating rating;

	private static final RatingType RATING_TYPE_GOOD = RatingType.GOOD;
	private static final Long AVATAR_ID = 2L;
	private static final Long REVIEW_ID = 123L;

	@Nested
	@DisplayName("getRatingInfoOf 메서드")
	class GetRatingInfoOf {
		@Test
		@DisplayName("Rating 레코드가 DB에 있으면 올바른 RatingInfoDto 를 반환한다.")
		void whenRatingExistThenRatedInfoDto() {
			//Arrange
			when(rating.getRatingType()).thenReturn(RATING_TYPE_GOOD);
			when(ratingRepository.findByAvatarAndReview(AVATAR_ID, REVIEW_ID)).thenReturn(Optional.of(rating));

			//Act
			RatingInfoDto ratingInfoDto = ratingService.getRatingInfoOf(AVATAR_ID, REVIEW_ID);

			//Assert
			assertThat(ratingInfoDto.getRatingType()).isEqualTo(RATING_TYPE_GOOD);
			assertThat(ratingInfoDto.isRated()).isTrue();
		}

		@Test
		@DisplayName("Rating 레코드가 DB에 없으면 평가하지 않았다는 Dto를 반환한다.")
		void whenRatingNotExistThenNotRatedDto() {
			//Arrange
			when(ratingRepository.findByAvatarAndReview(AVATAR_ID, REVIEW_ID)).thenReturn(Optional.empty());

			//Act
			RatingInfoDto ratingInfoDto = ratingService.getRatingInfoOf(AVATAR_ID, REVIEW_ID);

			//Assert
			assertThat(ratingInfoDto.getRatingType()).isNull();
			assertThat(ratingInfoDto.isRated()).isFalse();
		}
	}
}
