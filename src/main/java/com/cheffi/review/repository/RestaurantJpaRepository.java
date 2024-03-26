package com.cheffi.review.repository;

import static com.cheffi.review.domain.QRestaurant.*;

import java.util.List;

import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Repository;

import com.cheffi.review.constant.RestaurantStatus;
import com.cheffi.review.domain.Restaurant;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
public class RestaurantJpaRepository {

	private final EntityManager em;
	private final JPAQueryFactory queryFactory;

	public RestaurantJpaRepository(EntityManager em) {
		this.em = em;
		this.queryFactory = new JPAQueryFactory(em);
	}

	public List<Restaurant> findByPoint(Point currentPosition, int size, int radius) {
		return queryFactory.selectFrom(restaurant)
			.where(getBufferExpression(currentPosition, radius),
				restaurant.status.eq(RestaurantStatus.OPENED))
			.orderBy(restaurant.reviewCnt.desc(), getDistanceExpression(currentPosition).asc())
			.limit(size)
			.fetch();
	}

	private NumberExpression<Double> getDistanceExpression(Point point) {
		return Expressions.numberTemplate(Double.class, "ST_Distance_Sphere({0}, {1})",
			point, restaurant.coordinates);
	}

	private BooleanExpression getBufferExpression(Point point, int radius) {
		return Expressions.booleanTemplate("ST_CONTAINS(ST_BUFFER({0}, {1}), {2})",
			point, radius, restaurant.coordinates);
	}

}
