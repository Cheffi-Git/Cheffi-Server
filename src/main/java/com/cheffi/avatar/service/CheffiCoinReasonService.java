package com.cheffi.avatar.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.cheffi.avatar.constant.CfcReason;

@Service
public class CheffiCoinReasonService {

	private final DateTimeFormatter dailyRewardReasonFormatter;

	public CheffiCoinReasonService() {
		this.dailyRewardReasonFormatter = DateTimeFormatter.ofPattern("MM월 dd일");
	}

	public String getReasonForReviewUnlock(Long reviewId) {
		return CfcReason.PURCHASE_REVIEW.getReason();
	}

	public String getReasonForDailyReward(LocalDateTime now) {
		return dailyRewardReasonFormatter.format(now) + " " + CfcReason.DAILY_REWARD.getReason();
	}
}
