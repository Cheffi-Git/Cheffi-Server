package com.cheffi.review.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.EntityNotFoundException;
import com.cheffi.review.domain.RestaurantData;
import com.cheffi.review.dto.RestaurantInfoDto;
import com.cheffi.review.repository.RestaurantDataRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)

public class RestaurantDataService {
	private final RestaurantDataRepository restaurantDataRepository;

	public Page<RestaurantInfoDto> searchRestaurantByName(String name, Pageable pageable) {
		String keyword = name.trim().replace(" ", "");
		return restaurantDataRepository.findByNameStartsWith(keyword, pageable).map(RestaurantInfoDto::of);
	}

	public RestaurantData getRestaurantDataById(Long restaurantDataId) {
		return restaurantDataRepository.findById(restaurantDataId)
			.orElseThrow(() -> new EntityNotFoundException(ErrorCode.RESTAURANT_NOT_EXIST));
	}

}
