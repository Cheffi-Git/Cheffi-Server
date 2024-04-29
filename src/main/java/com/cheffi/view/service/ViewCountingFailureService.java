package com.cheffi.view.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.common.service.NamedLockRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
// @Transactional(readOnly = true)
@Service
public class ViewCountingFailureService {

	private final TempService tempService;
	private final NamedLockRepository namedLockRepository;
	private static final long BASE_MS = 200L;

	public void saveViewCountingFailure(Long reviewId) throws InterruptedException {

		for (int i = 0; i < 3; i++) {
			try {
				tempService.saveViewCountingFailure(reviewId);
				break;
			} catch (DataIntegrityViolationException e) {
				log.info(e.toString());
			}
		}

	}

}
