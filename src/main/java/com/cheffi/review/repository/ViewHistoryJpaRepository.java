package com.cheffi.review.repository;

import static com.cheffi.review.domain.QViewHistory.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.cheffi.review.dto.QScoreDto;
import com.cheffi.review.dto.ScoreDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
public class ViewHistoryJpaRepository {

	private final EntityManager em;
	private final JPAQueryFactory queryFactory;

	public ViewHistoryJpaRepository(EntityManager em) {
		this.em = em;
		this.queryFactory = new JPAQueryFactory(em);
	}

	public List<ScoreDto> countBetween(List<Long> ids, LocalDateTime from, LocalDateTime to) {
		JPAQuery<ScoreDto> select = queryFactory.from(viewHistory)
			.where(reviewIn(ids), createdBetween(from, to))
			.groupBy(viewHistory.review.id)
			.select(new QScoreDto(viewHistory.review.id, viewHistory.id.count().intValue()));

		return select.fetch();
	}

	private BooleanExpression reviewIn(List<Long> ids) {
		return viewHistory.review.id.in(ids);
	}

	private BooleanExpression createdBetween(LocalDateTime from, LocalDateTime to) {
		return viewHistory.createdDate.between(from, to);
	}

}
