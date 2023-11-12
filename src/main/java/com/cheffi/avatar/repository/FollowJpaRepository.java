package com.cheffi.avatar.repository;

import static com.cheffi.avatar.domain.QAvatar.*;
import static com.cheffi.avatar.domain.QFollow.*;
import static com.cheffi.avatar.domain.QProfilePhoto.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cheffi.avatar.domain.Follow;
import com.cheffi.common.request.CursorPageable;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
public class FollowJpaRepository {

	private final EntityManager em;
	private final JPAQueryFactory queryFactory;

	public FollowJpaRepository(EntityManager em) {
		this.em = em;
		this.queryFactory = new JPAQueryFactory(em);
	}

	public List<Follow> getFollows(Long avatarId, CursorPageable pageable) {

		return queryFactory
			.selectFrom(follow)
			.leftJoin(follow.subject, avatar)
			.leftJoin(avatar.photo, profilePhoto)
			.where(
				follow.subject.id.eq(avatarId),
				cursorCondition(pageable)
			)
			.limit(pageable.size())
			.fetch();

	}

	private BooleanExpression cursorCondition(CursorPageable pageable) {

		if (pageable.hasNextPageCursor()) {
			return follow.id.gt(Long.valueOf(pageable.getCursorValue()));
		}
		return null;
	}
}
