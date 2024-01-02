package com.cheffi.notification.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.avatar.service.FollowService;
import com.cheffi.common.dto.CursorPage;
import com.cheffi.notification.domain.Notification;
import com.cheffi.notification.dto.DeleteNotificationRequest;
import com.cheffi.notification.dto.GetNotificationRequest;
import com.cheffi.notification.dto.NotificationDto;
import com.cheffi.notification.repository.NotificationJdbcRepository;
import com.cheffi.notification.repository.NotificationJpaRepository;
import com.cheffi.notification.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NotificationService {

	private final NotificationJpaRepository notificationJpaRepository;
	private final NotificationJdbcRepository notificationJdbcRepository;
	private final NotificationRepository notificationRepository;
	private final FollowService followService;

	@Transactional
	public CursorPage<NotificationDto, Long> getNotifications(GetNotificationRequest request, Long avatarId) {
		List<NotificationDto> result = notificationJpaRepository.findByAvatar(request, avatarId)
			.stream()
			.map(NotificationDto::of)
			.toList();
		notificationJpaRepository.updateCheckedAllByAvatar(avatarId);

		return CursorPage.of(result, request.getSize(), NotificationDto::id);
	}

	@Transactional
	public List<Long> deleteNotifications(DeleteNotificationRequest request, Long avatarId) {
		List<Notification> notificationList;
		if (request.isDeleteAll())
			notificationList = notificationRepository.findAllByAvatar(avatarId);
		else
			notificationList = notificationRepository.findByAvatarAndId(avatarId, request.getNotifications());

		notificationRepository.deleteAll(notificationList);
		return notificationList.stream().map(Notification::getId).toList();
	}

	public Boolean existUncheckedNotification(Long avatarId) {
		return notificationRepository.existsUncheckedByAvatar(avatarId);
	}

	@Transactional
	public Integer notifyFollowersOfReviewCreation(Long writerId, final String nickname) {
		List<Notification> notifications = followService.getAllFollower(writerId).stream()
			.map(avatar -> Notification.ofReview(avatar, nickname)).toList();
		return notificationJdbcRepository.saveAll(notifications, "Review create event");
	}
}
