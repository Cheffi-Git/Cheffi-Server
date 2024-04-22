package com.cheffi.backoffice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cheffi.review.domain.Restaurant;
import com.cheffi.review.repository.RestaurantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminRestaurantService {

	private final RestaurantRepository restaurantRepository;

	public Page<Restaurant> findAllRes(Pageable pageable, String name) {
		return restaurantRepository.findByNameContaining(name, pageable);
	}
}
