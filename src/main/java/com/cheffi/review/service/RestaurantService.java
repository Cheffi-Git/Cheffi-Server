package com.cheffi.review.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.EntityNotFoundException;
import com.cheffi.review.domain.Restaurant;
import com.cheffi.review.dto.RestaurantInfoDto;
import com.cheffi.review.repository.RestaurantRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RestaurantService {
	private final RestaurantRepository restaurantRepository;

	public Page<RestaurantInfoDto> searchRestaurantByName(String name, Pageable pageable) {
		String keyword = name.trim().replace(" ", "");

		return restaurantRepository.findByNameContaining(keyword, pageable).map(RestaurantInfoDto::of);
	}

	public Restaurant getRestaurantById(Long restaurantId) {
		return restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new EntityNotFoundException(ErrorCode.RESTAURANT_NOT_EXIST));
	}

	public Restaurant registerRestaurant(Restaurant restaurant) {
		return restaurantRepository.save(restaurant);
	}

}
