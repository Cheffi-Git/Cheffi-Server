package com.cheffi.review.repository;

import static com.cheffi.review.domain.QRating.*;
import static com.cheffi.review.domain.QReview.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.cheffi.review.constant.RatingType;
import com.cheffi.review.dto.QScoreDto;
import com.cheffi.review.dto.ScoreDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
public class RatingJpaRepository {

	private final EntityManager em;
	private final JPAQueryFactory queryFactory;

	public RatingJpaRepository(EntityManager em) {
		this.em = em;
		this.queryFactory = new JPAQueryFactory(em);
	}

	public List<ScoreDto> countBetween(List<Long> ids, LocalDateTime from, LocalDateTime to) {
		JPAQuery<ScoreDto> select = queryFactory.from(review)
			.leftJoin(review.ratings, rating)
			.on(reviewIn(ids),
				createdBetween(from, to),
				positiveRating())
			.groupBy(review.id)
			.select(new QScoreDto(review.id, rating.count().intValue()));

		return select.fetch();
	}

	private BooleanExpression reviewIn(List<Long> ids) {
		return review.id.in(ids);
	}

	private BooleanExpression createdBetween(LocalDateTime from, LocalDateTime to) {
		return rating.createdDate.between(from, to);
	}

	private BooleanExpression positiveRating() {
		return rating.ratingType.eq(RatingType.AVERAGE).or(rating.ratingType.eq(RatingType.GOOD));
	}
}
