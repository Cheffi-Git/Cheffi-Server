package com.cheffi.common.constant;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DetailedAddressTest {

	private static final String PROVINCE = "서울시";
	private static final String CITY = "마포구";
	private static final String LOT_NUMBER = "숭문길 24";
	private static final String ROAD_NAME = "염리동 111";
	private static final String FULL_LOT_NUMBER_ADDRESS = PROVINCE + " " + CITY + " " + LOT_NUMBER;
	private static final String FULL_ROAD_NAME_ADDRESS = PROVINCE + " " + CITY + " " + ROAD_NAME;

	@Test
	@DisplayName("getFullLotNumberAddress 메서드를 호출하면 지번주소 전체를 반환한다.")
	void givenDetailedAddressWhenGetFullLotNumberAddress() {
		//when
		DetailedAddress address = new DetailedAddress(PROVINCE, CITY, LOT_NUMBER, ROAD_NAME);

		//then
		assertThat(address.getFullLotNumberAddress()).isEqualTo(FULL_LOT_NUMBER_ADDRESS);
	}

	@Test
	@DisplayName("getFullRodNameAddress 메서드를 호출하면 도로명 주소 전체를 반환한다.")
	void givenDetailedAddressWhenGetFullRoadNameAddress() {
		//when
		DetailedAddress address = new DetailedAddress(PROVINCE, CITY, LOT_NUMBER, ROAD_NAME);

		//then
		assertThat(address.getFullRodNameAddress()).isEqualTo(FULL_ROAD_NAME_ADDRESS);
	}

}
