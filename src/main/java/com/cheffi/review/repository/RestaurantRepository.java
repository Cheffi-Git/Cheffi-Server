package com.cheffi.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cheffi.review.domain.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

	@Query("select r from Restaurant r "
		+ "where r.nameForQuery like concat('%', :name, '%')"
		+ "order by r.reviewCnt desc ")
	Page<Restaurant> findByNameContaining(@Param("name") String name, Pageable pageable);

}
