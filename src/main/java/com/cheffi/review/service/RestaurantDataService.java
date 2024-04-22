package com.cheffi.review.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.EntityNotFoundException;
import com.cheffi.geo.dto.GeoQueryRequest;
import com.cheffi.geo.service.GeometryService;
import com.cheffi.review.domain.RestaurantData;
import com.cheffi.review.dto.RestaurantInfoDto;
import com.cheffi.review.dto.request.QueryRestaurantRequest;
import com.cheffi.review.repository.RestaurantDataJpaRepository;
import com.cheffi.review.repository.RestaurantDataRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)

public class RestaurantDataService {
	private final RestaurantDataRepository restaurantDataRepository;
	private final RestaurantDataJpaRepository restaurantDataJpaRepository;
	private final GeometryService geometryService;

	public Page<RestaurantInfoDto> searchRestaurantByName(String name, Pageable pageable) {
		String keyword = name.trim().replace(" ", "");
		return restaurantDataRepository.findByNameStartsWith(keyword, pageable).map(RestaurantInfoDto::of);
	}

	public RestaurantData getRestaurantDataById(Long restaurantDataId) {
		return restaurantDataRepository.findById(restaurantDataId)
			.orElseThrow(() -> new EntityNotFoundException(ErrorCode.RESTAURANT_NOT_EXIST));
	}

	public List<RestaurantInfoDto> searchRestaurantByPoint(QueryRestaurantRequest request, int size) {
		Point point = geometryService.getPoint(new GeoQueryRequest(request.getX(), request.getY()));
		return restaurantDataJpaRepository.findByPoint(point, size, 1000).stream().map(RestaurantInfoDto::of).collect(
			Collectors.toCollection(ArrayList::new));
	}
}
