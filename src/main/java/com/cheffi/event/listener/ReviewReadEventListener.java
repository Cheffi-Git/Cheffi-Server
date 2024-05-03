package com.cheffi.event.listener;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.cheffi.event.event.ReviewReadEvent;
import com.cheffi.review.service.ViewCountingService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ReviewReadEventListener {

	private final ViewCountingService viewCountingService;

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = ReviewReadEvent.class)
	public void onReadingIncreaseViewCount(ReviewReadEvent event) {
		viewCountingService.sendMessage(event);
	}

}
