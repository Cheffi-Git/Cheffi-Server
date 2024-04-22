package com.cheffi.notification.service

import com.cheffi.avatar.service.AvatarService
import com.cheffi.avatar.service.FollowService
import com.cheffi.common.dto.CursorPage
import com.cheffi.notification.domain.Notification
import com.cheffi.notification.dto.DeleteNotificationRequest
import com.cheffi.notification.dto.GetNotificationRequest
import com.cheffi.notification.dto.NotificationDto
import com.cheffi.notification.repository.NotificationJdbcRepository
import com.cheffi.notification.repository.NotificationJpaRepository
import com.cheffi.notification.repository.NotificationRepository
import com.cheffi.review.service.BookmarkService
import com.cheffi.util.model.ExactPeriod
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class NotificationService(
    private val notificationJpaRepository: NotificationJpaRepository,
    private val notificationJdbcRepository: NotificationJdbcRepository,
    private val notificationRepository: NotificationRepository,
    private val followService: FollowService,
    private val avatarService: AvatarService,
    private val bookmarkService: BookmarkService
) {

    @Transactional
    fun getNotifications(request: GetNotificationRequest, avatarId: Long): CursorPage<NotificationDto, Long> {
        val result = notificationJpaRepository.findByAvatar(request, avatarId).map(NotificationDto::of)
        notificationJpaRepository.updateCheckedAllByAvatar(avatarId)
        return CursorPage.of(result, request.size, NotificationDto::id)
    }

    @Transactional
    fun deleteNotifications(request: DeleteNotificationRequest, avatarId: Long): List<Long> {
        val notificationList =
            if (request.isDeleteAll) notificationRepository.findAllByAvatar(avatarId)
            else notificationRepository.findByAvatarAndId(avatarId, request.notifications)
        notificationRepository.deleteAll(notificationList)
        return notificationList.map { it.id }
    }

    fun existUncheckedNotification(avatarId: Long): Boolean {
        return notificationRepository.existsUncheckedByAvatar(avatarId)
    }

    @Transactional
    fun notifyFollowersOfReviewCreation(writerId: Long, nickname: String): List<Long> {
        val notifications = followService.getAllFollower(writerId)
            .map { avatar -> Notification.ofReview(avatar, nickname) }
        notificationJdbcRepository.saveAll(notifications, "Review create event")
        return notifications.map { it.id }
    }

    @Transactional
    fun notifyOfficialReview(title: String?): List<Long> {
        val avatars = avatarService.allActive
        val notifications = avatars.map { avatar -> Notification.ofOfficial(avatar, title) }
        notificationJdbcRepository.saveAll(notifications, "Official review create event")
        return notifications.map { it.id }
    }

    @Transactional
    fun notifyFollow(targetId: Long?, subjectNickname: String?): Long {
        return notificationRepository.save(Notification.ofFollow(avatarService.getById(targetId), subjectNickname)).id
    }

    @Transactional
    fun notifyReviewLocking(ep: ExactPeriod?): List<Long> {
        val notifications = bookmarkService.getByReviewLockBetween(ep)
            .map { b -> Notification.ofBookmark(b.avatar, b.review.title) }
        notificationJdbcRepository.saveAll(notifications, "Bookmarked review event")
        return notifications.map { it.id }
    }
}
