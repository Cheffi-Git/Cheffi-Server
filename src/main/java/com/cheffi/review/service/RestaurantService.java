package com.cheffi.review.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.BusinessException;
import com.cheffi.common.config.exception.business.EntityNotFoundException;
import com.cheffi.region.service.RegionService;
import com.cheffi.review.domain.Restaurant;
import com.cheffi.review.dto.RestaurantInfoDto;
import com.cheffi.review.dto.request.RegisterRestaurantRequest;
import com.cheffi.review.repository.RestaurantRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RestaurantService {

	private final RestaurantRepository restaurantRepository;
	private final RegionService regionService;

	public Page<RestaurantInfoDto> searchRestaurantByName(String name, Pageable pageable) {
		String keyword = name.trim().replace(" ", "");

		return restaurantRepository.findByNameContaining(keyword, pageable).map(RestaurantInfoDto::of);
	}

	@Transactional
	public Long registerTempRestaurant(RegisterRestaurantRequest request) {

		if (!regionService.contains(request.getDetailedAddress()))
			throw new BusinessException(ErrorCode.ADDRESS_NOT_EXIST);

		return registerRestaurant(Restaurant.registerTempRestaurant(request))
			.getId();
	}

	@Transactional
	public Restaurant registerRestaurant(Restaurant restaurant) {
		return restaurantRepository.save(restaurant);
	}

	public Restaurant getRestaurantById(Long restaurantId) {
		return restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new EntityNotFoundException(ErrorCode.RESTAURANT_NOT_EXIST));
	}
}
