package com.cheffi.notification.repository;

import static com.cheffi.notification.domain.QNotification.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cheffi.notification.domain.Notification;
import com.cheffi.notification.dto.GetNotificationRequest;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
public class NotificationJpaRepository {

	private final EntityManager em;
	private final JPAQueryFactory queryFactory;

	public NotificationJpaRepository(EntityManager em) {
		this.em = em;
		this.queryFactory = new JPAQueryFactory(em);
	}

	public List<Notification> findByAvatar(GetNotificationRequest request, Long avatarId) {
		JPAQuery<Notification> query = queryFactory
			.selectFrom(notification)
			.where(notification.target.id.eq(avatarId),
				notification.id.loe(request.getCursor(Long.MAX_VALUE)))
			.orderBy(notification.id.desc())
			.limit(request.getSize() + 1L);

		return query.fetch();
	}

	public Long updateCheckedAllByAvatar(Long avatarId) {
		return queryFactory.update(notification)
			.set(notification.checked, Expressions.TRUE)
			.where(notification.target.id.eq(avatarId))
			.execute();
	}
}
