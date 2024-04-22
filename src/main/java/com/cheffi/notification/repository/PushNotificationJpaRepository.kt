package com.cheffi.notification.repository

import com.cheffi.avatar.domain.QAvatar.avatar
import com.cheffi.notification.domain.PushNotification
import com.cheffi.notification.domain.QDevice.device
import com.cheffi.notification.domain.QNotification.notification
import com.cheffi.notification.domain.QPushNotification
import com.cheffi.user.domain.QUser.user
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository

@Repository
class PushNotificationJpaRepository(
    private val em: EntityManager
) {
    private val queryFactory: JPAQueryFactory = JPAQueryFactory(em)

    fun findPushNotificationById(ids: List<Long>): List<PushNotification> {
        return queryFactory.query()
            .select(QPushNotification(notification, device))
            .from(notification)
            .join(notification.target, avatar)
            .join(avatar.user, user)
            .join(user.devices, device)
            .where(notification.id.`in`(ids), user.notificationAllowed.isTrue)
            .fetch()
    }


}
