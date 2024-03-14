package com.cheffi;

import java.util.List;

import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import com.cheffi.common.constant.DetailedAddress;
import com.cheffi.geo.dto.GeoQueryRequest;
import com.cheffi.geo.service.GeometryService;
import com.cheffi.region.service.RegionDto;
import com.cheffi.region.service.RegionService;
import com.cheffi.review.constant.RestaurantStatus;
import com.cheffi.review.domain.Restaurant;
import com.cheffi.review.repository.RestaurantRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DummyRestaurantMaker {

	private final RegionService regionService;
	private final GeometryService geometryService;
	private final RestaurantRepository restaurantRepository;

	/**
	 * 각 2차 주소별로 size 만큼의 식당을 등록하는 로직
	 *
	 * @param size
	 */
	public void makeRestaurant(int size) {
		List<RegionDto> regions = regionService.getRegion();
		Point zero = geometryService.getPoint(new GeoQueryRequest(0, 0));
		for (var region : regions) {
			String province = region.getProvince();
			List<String> cities = region.getCities();
			for (int i = 0; i < cities.size(); i++) {
				String city = cities.get(i);
				DetailedAddress address = DetailedAddress.of(province, city, "TEST 지번주소", "TEST 도로명주소");
				for (int j = 1; j <= size; j++) {
					String name = "TEST " + city + " 식당" + j;
					Restaurant restaurant = new Restaurant(name, address, "TEST", zero, RestaurantStatus.OPENED);
					restaurantRepository.save(restaurant);
				}
			}
		}
	}

}
