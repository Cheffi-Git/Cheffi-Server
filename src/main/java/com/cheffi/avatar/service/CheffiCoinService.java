package com.cheffi.avatar.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.avatar.domain.CheffiCoin;
import com.cheffi.avatar.repository.CheffiCoinRepository;
import com.cheffi.review.domain.Review;
import com.cheffi.review.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CheffiCoinService {

	private static final int CHEFFI_COIN_FOR_REVIEW = -1;
	private static final int CHEFFI_COIN_FOR_LOGIN = 5;
	private final CheffiCoinRepository cheffiCoinRepository;
	private final AvatarService avatarService;
	private final ReviewService reviewService;

	@Transactional
	public void useCheffiCoinForUnlock(Long avatarId, Long reviewId) {
		Review review = reviewService.getById(reviewId);
		CheffiCoin cheffiCoin = CheffiCoin.of(avatarService.getById(avatarId), CHEFFI_COIN_FOR_REVIEW,
			"'" + review.getTitle() + "' 게시글을 조회하는데 사용했습니다.");
		cheffiCoinRepository.save(cheffiCoin);
	}

	@Transactional
	public void earnCheffiCoinForLogin(Long avatarId) {
		CheffiCoin cheffiCoin = CheffiCoin.of(avatarService.getById(avatarId)
			, CHEFFI_COIN_FOR_LOGIN, "매일 접속 쉐피 코인 획득! 날짜: " + LocalDate.now());
		cheffiCoinRepository.save(cheffiCoin);
	}
}
