package com.cheffi.review.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cheffi.common.constant.DetailedAddress;
import com.cheffi.review.constant.RestaurantStatus;

@ExtendWith(MockitoExtension.class)
class RestaurantTest {

	private static final String RESTAURANT_NAME = "중앙 해장";
	private static final String RESTAURANT_NAME_FOR_QUERY = "중앙해장";
	@Mock
	private DetailedAddress detailedAddress;

	@Test
	@DisplayName("Restaurant 생성자는 쿼리용으로 식당이름에서 공백을 제거한 nameForQuery를 갖는다.")
	void givenRestaurantWhenConstruct() {
		//when
		Restaurant restaurant = new Restaurant(RESTAURANT_NAME, detailedAddress, RestaurantStatus.OPENED);

		//then
		assertThat(restaurant.getNameForQuery()).isEqualTo(RESTAURANT_NAME_FOR_QUERY);

	}

}
