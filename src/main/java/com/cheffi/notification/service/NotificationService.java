package com.cheffi.notification.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.common.dto.CursorPage;
import com.cheffi.notification.domain.Notification;
import com.cheffi.notification.dto.DeleteNotificationRequest;
import com.cheffi.notification.dto.GetNotificationRequest;
import com.cheffi.notification.dto.NotificationDto;
import com.cheffi.notification.repository.NotificationJpaRepository;
import com.cheffi.notification.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NotificationService {

	private final NotificationJpaRepository notificationJpaRepository;
	private final NotificationRepository notificationRepository;

	// 1. 커밋 컨벤션 [FEAT]
	// 2. 코드 컨벤션
	// 3. 협업 전략? 혹은 코드 합병 전략
	void method() {

	}

	@Transactional
	public CursorPage<NotificationDto, Long> getNotifications(GetNotificationRequest request, Long avatarId) {
		List<Notification> notifications = notificationJpaRepository.findByAvatar(request, avatarId);
		List<NotificationDto> result = notifications.stream().map(NotificationDto::of).toList();

		notifications.stream()
			.filter(Notification::isUnchecked)
			.forEach(Notification::check);

		return CursorPage.of(result, request.getSize(),
			NotificationDto::id);
	}

	@Transactional
	public List<Long> deleteNotifications(DeleteNotificationRequest request, Long avatarId) {
		if (request.isDeleteAll())
			return deleteAll(avatarId);

		//TODO where 문 추가
		List<Notification> notificationList = notificationRepository.findAllById(request.getNotifications());
		notificationRepository.deleteAll(notificationList);
		return notificationList.stream().map(Notification::getId).toList();
	}

	private List<Long> deleteAll(Long avatarId) {
		List<Notification> notificationList = notificationRepository.findAllByAvatar(avatarId);
		notificationRepository.deleteAll(notificationList);
		return notificationList.stream().map(Notification::getId).toList();
	}
}
