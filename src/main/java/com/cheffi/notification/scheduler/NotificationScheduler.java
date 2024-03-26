package com.cheffi.notification.scheduler;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cheffi.notification.service.NotificationService;
import com.cheffi.util.model.ExactPeriod;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class NotificationScheduler {

	private final NotificationService notificationService;

	@Scheduled(cron = "0 * * * * *")
	public void notifyBookmarkedReviewLocking() {
		ExactPeriod ep = new ExactPeriod(LocalDateTime.now().plusMinutes(61L), Duration.ofMinutes(1),
			ChronoUnit.MINUTES);
		notificationService.notifyReviewLocking(ep);
	}

}
