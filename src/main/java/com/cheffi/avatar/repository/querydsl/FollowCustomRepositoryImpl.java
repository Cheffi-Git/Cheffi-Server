package com.cheffi.avatar.repository.querydsl;

import java.util.List;

import com.cheffi.avatar.domain.Follow;
import com.cheffi.avatar.domain.QAvatar;
import com.cheffi.avatar.domain.QFollow;
import com.cheffi.avatar.domain.QProfilePhoto;
import com.cheffi.common.request.CursorPageable;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FollowCustomRepositoryImpl implements FollowCustomRepository {

	private final JPQLQueryFactory jpqlQueryFactory;

	private final QFollow follow = QFollow.follow;
	private final QAvatar avatar = QAvatar.avatar;
	private final QProfilePhoto profilePhoto = QProfilePhoto.profilePhoto;

	@Override
	public List<Follow> getFollows(Long avatarId, CursorPageable pageable) {

		return jpqlQueryFactory
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

	@Override
	public boolean existsFollowsAfterId(Long followId) {

		long count = jpqlQueryFactory
			.selectFrom(follow)
			.where(follow.id.gt(followId))
			.limit(1)
			.fetchCount();

		return count > 0;
	}

	private BooleanExpression cursorCondition(CursorPageable pageable) {

		if (pageable.hasNextPageCursor()) {
			return follow.id.gt(Long.valueOf(pageable.getCursorValue()));
		}
		return null;
	}
}
