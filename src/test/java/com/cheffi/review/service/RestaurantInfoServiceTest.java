package com.cheffi.review.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cheffi.review.domain.Restaurant;
import com.cheffi.review.domain.RestaurantData;

@ExtendWith(MockitoExtension.class)
class RestaurantInfoServiceTest {

	@Mock
	private RestaurantService restaurantService;
	@Mock
	private RestaurantDataService restaurantDataService;
	@InjectMocks
	private RestaurantInfoService restaurantInfoService;

	@Nested
	@DisplayName("getOrRegisterById 메서드")
	class GetOrRegisterById {
		@Mock
		private Restaurant registeredRestaurant;
		@Mock
		private Restaurant newRestaurant;
		@Mock
		private RestaurantData restaurantData;

		private static final Long REGISTERED_RESTAURANT_ID = 10L;
		private static final Long UNREGISTERED_RESTAURANT_ID = 300020L;

		@Nested
		@DisplayName("registered flag 가 true면")
		class RegisteredRestaurant {
			@Test
			@DisplayName("RestaurantRepository 에 데이터가 존재할 때 Restaurant 엔티티를 반환한다.")
			void givenValidId() {
				//given
				when(restaurantService.getRestaurantById(REGISTERED_RESTAURANT_ID))
					.thenReturn(registeredRestaurant);

				//when
				Restaurant result = restaurantInfoService.getOrRegisterById(REGISTERED_RESTAURANT_ID, true);

				//then
				assertThat(result).isEqualTo(registeredRestaurant);

			}

		}

		@Nested
		@DisplayName("registered flag 가 false면")
		class UnregisteredRestaurant {

			@Test
			@DisplayName("RestaurantDataRepository 에 데이터가 존재할 때 데이터를 식당 엔티티로 바꾼 뒤 save하고 Restaurant 엔티티를 반환한다.")
			void givenValidId() {
				//given
				when(restaurantDataService.getRestaurantDataById(UNREGISTERED_RESTAURANT_ID))
					.thenReturn(restaurantData);
				when(restaurantData.toRestaurant()).thenReturn(newRestaurant);
				when(restaurantService.registerRestaurant(newRestaurant)).thenReturn(newRestaurant);

				//when
				Restaurant result = restaurantInfoService.getOrRegisterById(UNREGISTERED_RESTAURANT_ID, false);

				//then
				assertThat(result).isEqualTo(newRestaurant);

			}

		}

	}
}
