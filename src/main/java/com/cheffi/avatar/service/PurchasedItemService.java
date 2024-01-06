package com.cheffi.avatar.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.avatar.domain.Avatar;
import com.cheffi.avatar.domain.PurchasedItem;
import com.cheffi.avatar.repository.PurchasedItemRepository;
import com.cheffi.review.domain.Review;
import com.cheffi.review.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PurchasedItemService {

	private final PurchasedItemRepository purchasedItemRepository;
	private final AvatarService avatarService;
	private final ReviewService reviewService;
	private final CheffiCoinService cheffiCoinService;

	@Transactional
	public PurchasedItem unlock(Long avatarId, Long reviewId) {
		Avatar avatar = avatarService.getById(avatarId);
		Review review = reviewService.getById(reviewId);
		cheffiCoinService.useCheffiCoinForUnlock(avatarId, reviewId);
		return purchasedItemRepository.save(new PurchasedItem(avatar, review));
	}

	public boolean hasUnlocked(Long avatarId, Long reviewId) {
		return purchasedItemRepository.existByAvatarAndReview(avatarId, reviewId);
	}

	public List<PurchasedItem> getPurchasedItems(Long avatarId) {
		return purchasedItemRepository.findByAvatar(avatarId);
	}

}
