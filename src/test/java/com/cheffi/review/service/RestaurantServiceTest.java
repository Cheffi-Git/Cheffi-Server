package com.cheffi.review.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cheffi.review.domain.Restaurant;
import com.cheffi.review.dto.RestaurantInfoDto;
import com.cheffi.review.repository.RestaurantRepository;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

	@Mock
	private RestaurantRepository restaurantRepository;

	@InjectMocks
	private RestaurantService restaurantService;

	@Nested
	@DisplayName("searchRestaurantByName 메서드")
	class SearchRestaurantByName {

		private static final String KEYWORD_WITHOUT_SPACES = "중앙해장";
		private static final String KEYWORD_WITH_SPACES = " 중앙 해장    ";

		@Mock
		private Pageable pageable;
		@Mock
		private Page<Restaurant> resultWithNoSpaces;
		@Mock
		private Page<RestaurantInfoDto> dtoWithNoSpaces;

		@Test
		@DisplayName("공백이 있는 검색어가 주어져도 공백을 제거하고 쿼리한다.")
		void givenNameWithSpacesWhenSearchByNameThenReturnResultWithoutSpaces() {
			//given
			when(restaurantRepository.findByNameContaining(KEYWORD_WITHOUT_SPACES, pageable))
				.thenReturn(resultWithNoSpaces);

			when(resultWithNoSpaces.map(any(Function.class))).thenReturn(dtoWithNoSpaces);

			//when
			Page<RestaurantInfoDto> page = restaurantService.searchRestaurantByName(KEYWORD_WITH_SPACES, pageable);

			//then
			assertThat(page).isEqualTo(dtoWithNoSpaces);

		}

	}

}
