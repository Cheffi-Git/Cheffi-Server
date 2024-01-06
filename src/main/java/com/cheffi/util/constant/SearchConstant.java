package com.cheffi.util.constant;

import java.time.Duration;

public class SearchConstant {

	private SearchConstant() {
	}

	public static final Duration TRENDING_UPDATE_CYCLE = Duration.ofHours(1);
	public static final Duration TAG_UPDATE_CYCLE = Duration.ofDays(30);
}
