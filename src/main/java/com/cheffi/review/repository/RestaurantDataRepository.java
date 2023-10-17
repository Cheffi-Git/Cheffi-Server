package com.cheffi.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cheffi.review.domain.RestaurantData;

public interface RestaurantDataRepository extends JpaRepository<RestaurantData, Long> {
	@Query("select r from RestaurantData r "
		+ "where r.nameForQuery like concat(:name, '%') "
		+ "and r.registered = false ")
	Page<RestaurantData> findByNameStartsWith(@Param("name") String keyword, Pageable pageable);
}
