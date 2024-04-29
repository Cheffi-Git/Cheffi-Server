package com.cheffi.event.listener;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.cheffi.common.service.NamedLockRepository;
import com.cheffi.event.event.NamedLockEvent;
import com.cheffi.event.event.ReviewReadEvent;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class NamedLockEventListener {

	private final NamedLockRepository namedLockRepository;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION, classes = NamedLockEvent.class)
	public void onReadingIncreaseViewCount(NamedLockEvent event) {
		namedLockRepository.releaseLock(event.getLockName());
	}


}
