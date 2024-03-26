package com.cheffi.avatar.repository;

import static com.cheffi.avatar.domain.QCheffiCoin.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cheffi.avatar.constant.CfcHistoryCategory;
import com.cheffi.avatar.domain.CheffiCoin;
import com.cheffi.avatar.dto.CheffiCoinHistoryRequest;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
public class CheffiCoinJpaRepository {

	private final EntityManager em;
	private final JPAQueryFactory queryFactory;

	public CheffiCoinJpaRepository(EntityManager em) {
		this.em = em;
		this.queryFactory = new JPAQueryFactory(em);
	}

	public List<CheffiCoin> findCheffiCoinHistory(Long avatarId, CheffiCoinHistoryRequest request) {

		JPAQuery<CheffiCoin> query = queryFactory.selectFrom(cheffiCoin)
			.where(cheffiCoin.avatar.id.eq(avatarId),
				cheffiCoin.id.goe(request.getCursor()),
				categoryCondition(request.getCategory())
			)
			.limit(request.getSize() + 1L);

		return query.fetch();
	}

	private BooleanExpression categoryCondition(CfcHistoryCategory category) {
		if (category == null)
			return null;
		if (category.equals(CfcHistoryCategory.PLUS))
			return cheffiCoin.cfcValue.goe(0);
		return cheffiCoin.cfcValue.lt(0);
	}
}
