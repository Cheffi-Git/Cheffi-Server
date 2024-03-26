package com.cheffi.cs.repository;

import static com.cheffi.cs.domain.QQna.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cheffi.cs.constant.QnaCategory;
import com.cheffi.cs.domain.Qna;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
public class QnaJpaRepository {

	private final EntityManager em;
	private final JPAQueryFactory queryFactory;

	public QnaJpaRepository(EntityManager em) {
		this.em = em;
		this.queryFactory = new JPAQueryFactory(em);
	}

	public List<Qna> findByCategory(QnaCategory category) {
		return queryFactory.selectFrom(qna)
			.where(categoryEq(category))
			.fetch();
	}

	private BooleanBuilder categoryEq(QnaCategory category) {
		BooleanBuilder builder = new BooleanBuilder();
		if (QnaCategory.ALL.equals(category))
			return builder;
		if (QnaCategory.BEST.equals(category))
			return builder.and(qna.best.isTrue());
		return builder.and(qna.category.eq(category));
	}

}
