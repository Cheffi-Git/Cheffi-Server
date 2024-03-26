package com.cheffi.event.listener;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.cheffi.event.event.ReviewCreateEvent;
import com.cheffi.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class NotificationEventListener {

	private final NotificationService notificationService;

	/**
	 * `@Async` 를 사용하거나 `JDBC`를 사용한다면
	 * `@Transactional(propagation = Propagation.REQUIRES_NEW)`가 없어도 Insert 가 된다.
	 * 공식 게시글이라면 모든 활성화된 유저에게, 아니라면 작성자의 팔로워들에게 알림을 전송한다.
	 */
	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = ReviewCreateEvent.class)
	public void onReviewCreation(ReviewCreateEvent event) {
		if (event.getAuthorities().containsAdminRole())
			notificationService.notifyOfficialReview(event.getTitle());
		else
			notificationService.notifyFollowersOfReviewCreation(event.getWriterId(), event.getNickname());
	}
}
