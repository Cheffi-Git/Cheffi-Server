package com.cheffi.avatar.repository;

import static com.cheffi.avatar.domain.QAvatar.*;
import static com.cheffi.avatar.domain.QAvatarTag.*;
import static com.cheffi.avatar.domain.QFollow.*;
import static com.cheffi.cs.domain.QBlock.*;
import static com.cheffi.tag.domain.QTag.*;
import static com.querydsl.core.group.GroupBy.*;

import org.springframework.stereotype.Repository;

import com.cheffi.profile.dto.ProfileInfo;
import com.cheffi.profile.dto.QProfileInfo;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
public class AvatarJpaRepository {

	private final EntityManager em;
	private final JPAQueryFactory queryFactory;

	/**
	 * @author wnsvy607
	 * `JPQLTemplates.DEFAULT` 는
	 * Spring Boot 3.x 부터 QueryDSL 의 transform 메서드가 오작동하기 때문에
	 * 추가한 매개변수이다.
	 * @see <a href="https://github.com/querydsl/querydsl/issues/3428">참고</a>
	 */
	public AvatarJpaRepository(EntityManager em) {
		this.em = em;
		this.queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, em);
	}

	public ProfileInfo findProfile(Long ownerId, Long viewerId) {
		boolean authenticated = viewerId != null;

		JPAQuery<?> common = queryFactory
			.from(avatar)
			.leftJoin(avatar.avatarTags, avatarTag)
			.leftJoin(avatarTag.tag, tag)
			.where(avatar.id.eq(ownerId));

		if (authenticated)
			common.leftJoin(follow)
				.on(follow.subject.id.eq(viewerId),
					follow.target.id.eq(ownerId))
				.leftJoin(block)
				.on(block.subject.id.eq(viewerId),
					block.target.id.eq(ownerId));

		return select(common, authenticated);
	}

	private ProfileInfo select(JPAQuery<?> common, boolean authenticated) {
		return common.transform(groupBy(avatar.id).list(new QProfileInfo(
			avatar,
			avatar.photo.url,
			authenticated ? follow.isNotNull() : Expressions.nullExpression(),
			authenticated ? block.isNotNull() : Expressions.nullExpression(),
			list(tag)
		))).get(0);
	}
}
