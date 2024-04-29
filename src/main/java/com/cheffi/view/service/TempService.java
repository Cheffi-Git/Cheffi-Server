package com.cheffi.view.service;

import java.util.Optional;
import java.util.Random;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.common.service.NamedLockRepository;
import com.cheffi.event.event.NamedLockEvent;
import com.cheffi.view.domain.ViewCountingFailure;
import com.cheffi.view.repository.ViewCountingFailureRepository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TempService {

	private final ViewCountingFailureRepository viewCountingFailureRepository;
	private final NamedLockRepository namedLockRepository;
	private final ApplicationEventPublisher eventPublisher;
	private static final long BASE_MS = 200L;
	private final EntityManager em;

	@Transactional
	public void saveViewCountingFailure(Long reviewId) throws InterruptedException {
		int attempts = 0;
		boolean lockAcquired = false;
		Random random = new Random();

		while (attempts < 10 && !lockAcquired) {
			int lock = namedLockRepository.performCriticalSection(getLockName(reviewId), 10);
			if (lock == 1) {
				eventPublisher.publishEvent(new NamedLockEvent(getLockName(reviewId)));
				lockAcquired = true;
				Optional<ViewCountingFailure> failure = getOptionalById(reviewId);
				if (failure.isEmpty()) {
					//새로 저장
					viewCountingFailureRepository.save(new ViewCountingFailure(reviewId, 1));
					return;
				}
				failure.get().increaseCount();
			} else {
				attempts++;
				Thread.sleep(BASE_MS + random.nextInt(18) * 100);
				log.info("lock 얻지 못함");
			}
		}
	}

	public Optional<ViewCountingFailure> getOptionalById(Long reviewId) {
		return viewCountingFailureRepository.findById(reviewId);
	}

	private String getLockName(Long reviewId) {
		return "Review" + reviewId;
	}
}
