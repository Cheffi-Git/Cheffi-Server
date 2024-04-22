package com.cheffi.avatar.repository;

import static com.cheffi.avatar.domain.QAvatarTag.*;
import static com.cheffi.avatar.domain.QFollow.*;
import static com.cheffi.avatar.domain.QProfilePhoto.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cheffi.avatar.domain.QAvatar;
import com.cheffi.avatar.domain.QFollow;
import com.cheffi.avatar.dto.FollowCursor;
import com.cheffi.avatar.dto.GetFollowRequest;
import com.cheffi.avatar.dto.QFollowCursor;
import com.cheffi.avatar.dto.response.GetFollowResponse;
import com.cheffi.avatar.dto.response.QGetFollowResponse;
import com.cheffi.avatar.dto.response.QRecommendFollowResponse;
import com.cheffi.avatar.dto.response.RecommendFollowResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
public class FollowJpaRepository {

	private final EntityManager em;
	private final JPAQueryFactory queryFactory;
	private final QAvatar recommended = new QAvatar("recommended");
	private final QAvatar object = new QAvatar("object");
	private final QFollow viewerFollow = new QFollow("viewerFollow");

	public FollowJpaRepository(EntityManager em) {
		this.em = em;
		this.queryFactory = new JPAQueryFactory(em);
	}

	public List<RecommendFollowResponse> recommendByTag(List<Long> tags, Long avatarId) {
		JPAQuery<RecommendFollowResponse> query = queryFactory
			.from(recommended)
			.select(new QRecommendFollowResponse(
				recommended.id,
				recommended.nickname.value,
				profilePhoto.url,
				recommended.introduction,
				recommended.followerCnt,
				follow.isNotNull()
			))
			.leftJoin(recommended.photo, profilePhoto)
			.leftJoin(follow)
			.on(follow.target.id.eq(recommended.id),
				follow.subject.id.eq(avatarId))
			.leftJoin(recommended.avatarTags, avatarTag)
			.where(avatarTag.tag.id.in(tags),
				follow.isNull(),
				avatarTag.isNotNull(),
				recommended.id.ne(avatarId))
			.orderBy(recommended.followerCnt.desc())
			.limit(12);

		return query.fetch();
	}

	public List<GetFollowResponse> findFollowingByCursor(GetFollowRequest request, Long followerId, Long viewerId) {

		JPAQuery<?> common = queryFactory
			.from(follow)
			.innerJoin(object)
			.on(follow.target.id.eq(object.id),
				follow.subject.id.eq(followerId))
			.leftJoin(object.photo, profilePhoto)
			.limit(request.getSize() + 1L);
		if (viewerId == null) {
			return selectSimple(common, request.getCursor(), Expressions.FALSE).fetch();
		}

		if (followerId.equals(viewerId))
			return selectSimple(common, request.getCursor(), Expressions.TRUE).fetch();

		return selectWithViewer(common, getBooleanBuilder(request.getCursor(), viewerId, follow.target.id),
			viewerId).fetch();
	}

	public List<GetFollowResponse> findFollowerByCursor(GetFollowRequest request, Long followingId, Long viewerId) {

		JPAQuery<?> common = queryFactory
			.from(follow)
			.innerJoin(object)
			.on(follow.target.id.eq(followingId),
				follow.subject.id.eq(object.id))
			.leftJoin(object.photo, profilePhoto)
			.limit(request.getSize() + 1L);
		if (viewerId == null) {
			return selectSimple(common, request.getCursor(), Expressions.FALSE).fetch();
		}

		return selectWithViewer(common, getBooleanBuilder(request.getCursor(), viewerId, follow.subject.id),
			viewerId).fetch();
	}

	private BooleanBuilder getBooleanBuilder(Long cursor, Long viewerId, NumberPath<Long> selectItem) {
		BooleanBuilder builder = new BooleanBuilder();
		if (cursor == null) {
			return builder;
		}
		FollowCursor followCursor = getFollowCursor(cursor, viewerId, selectItem);

		if (followCursor.isFollowedByViewer()) {
			return builder.and(viewerFollow.isNotNull().and(follow.id.loe(cursor)))
				.or(viewerFollow.isNull().and(selectItem.ne(viewerId)));
		}

		return builder
			.and(viewerFollow.isNull())
			.and(follow.id.loe(cursor))
			.and(selectItem.ne(viewerId));
	}

	public FollowCursor getFollowCursor(Long cursor, Long viewerId, NumberPath<Long> criterion) {
		return queryFactory
			.select(new QFollowCursor(
				follow.id,
				viewerFollow.isNotNull()
			))
			.from(follow)
			.leftJoin(viewerFollow)
			.on(criterion.eq(viewerFollow.target.id),
				viewerFollow.subject.id.eq(viewerId))
			.where(follow.id.eq(cursor))
			.fetchOne();
	}

	private JPAQuery<GetFollowResponse> selectSimple(JPAQuery<?> common, Long cursor, BooleanExpression expression) {
		return common.select(new QGetFollowResponse(
				follow.id,
				object.id,
				object.nickname.value,
				profilePhoto.url,
				expression
			))
			.where(cursor == null ? new BooleanBuilder() : follow.id.loe(cursor))
			.orderBy(follow.id.desc())
			;
	}

	private JPAQuery<GetFollowResponse> selectWithViewer(JPAQuery<?> common, BooleanBuilder condition, Long viewerId) {
		return common.select(new QGetFollowResponse(
				follow.id,
				object.id,
				object.nickname.value,
				profilePhoto.url,
				viewerFollow.isNotNull()
			))
			.leftJoin(viewerFollow)
			.on(viewerFollow.subject.id.eq(viewerId),
				viewerFollow.target.id.eq(object.id))
			.where(condition)
			.orderBy(followOrderBy(viewerId), follow.id.desc());
	}

	private OrderSpecifier<Integer> followOrderBy(Long viewerId) {
		return new CaseBuilder()
			.when(object.id.eq(viewerId)).then(1)
			.when(viewerFollow.isNotNull()).then(2)
			.otherwise(3).asc();
	}

}
