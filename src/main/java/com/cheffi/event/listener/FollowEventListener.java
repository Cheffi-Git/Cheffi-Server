package com.cheffi.event.listener;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.cheffi.event.event.FollowEvent;
import com.cheffi.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class FollowEventListener {

	private final NotificationService notificationService;

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = FollowEvent.class)
	public void onReviewCreation(FollowEvent event) {
		notificationService.notifyFollow(event.getTargetId(), event.getSubjectNickname());
	}
}
