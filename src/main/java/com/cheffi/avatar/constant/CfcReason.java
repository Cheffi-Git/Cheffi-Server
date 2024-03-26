package com.cheffi.avatar.constant;

public enum CfcReason {
	PURCHASE_REVIEW("잠긴 리뷰 조회"), DAILY_REWARD("접속 보상");
	private final String reason;

	CfcReason(String reason) {
		this.reason = reason;
	}

	public String getReason() {
		return reason;
	}
}
