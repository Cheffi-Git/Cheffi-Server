package com.cheffi.avatar.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.BusinessException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PurchasedItemInfoService {

	private final PurchasedItemService purchasedItemService;

	@Transactional
	public Long purchaseReview(Long avatarId, Long reviewId) {
		if (purchasedItemService.hasUnlocked(avatarId, reviewId))
			throw new BusinessException(ErrorCode.REVIEW_ALREADY_UNLOCKED);
		purchasedItemService.unlock(avatarId, reviewId);
		return reviewId;
	}
}
