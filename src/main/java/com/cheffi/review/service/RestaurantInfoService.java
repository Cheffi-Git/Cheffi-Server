package com.cheffi.review.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.review.domain.Restaurant;
import com.cheffi.review.domain.RestaurantData;
import com.cheffi.review.dto.RestaurantInfoDto;
import com.cheffi.review.dto.request.QueryRestaurantRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RestaurantInfoService {

	private final RestaurantService restaurantService;
	private final RestaurantDataService restaurantDataService;

	public List<RestaurantInfoDto> searchRestaurantByName(String name, PageRequest request) {
		List<RestaurantInfoDto> result = new ArrayList<>();
		restaurantService.searchRestaurantByName(name, request).forEach(result::add);
		if (result.size() < 10) {
			restaurantDataService.searchRestaurantByName(name, PageRequest.of(0, 10 - result.size()))
				.forEach(result::add);
		}
		return result;
	}

	public Restaurant getOrRegisterById(Long targetId, boolean registered) {
		if (registered)
			return restaurantService.getRestaurantById(targetId);
		RestaurantData restaurantData = restaurantDataService.getRestaurantDataById(targetId);

		return restaurantService.registerRestaurant(restaurantData.toRestaurant());
	}

	public List<RestaurantInfoDto> getPopularRestaurantsWithin(QueryRestaurantRequest request) {
		List<RestaurantInfoDto> result = restaurantService.searchRestaurantByPoint(request, 8);
		if (result.size() < 8) {
			result.addAll(restaurantDataService.searchRestaurantByPoint(request, 8 - result.size()));
		}
		return result;
	}
}
