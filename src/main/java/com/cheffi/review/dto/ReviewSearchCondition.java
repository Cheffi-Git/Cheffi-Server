package com.cheffi.review.dto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.cheffi.common.constant.Address;

import lombok.Getter;

@Getter
public class ReviewSearchCondition {

	private final Address address;
	private final Long tagId;
	private final LocalDateTime referenceTime;
	private final Duration searchPeriod;
	private final ChronoUnit unit;

	public ReviewSearchCondition(Address address, LocalDateTime referenceTime, Duration searchPeriod, ChronoUnit unit,
		Long tagId
	) {
		this.address = address;
		this.referenceTime = referenceTime;
		this.searchPeriod = searchPeriod;
		this.unit = unit;
		this.tagId = tagId;
	}

	public boolean noTagCondition() {
		return this.tagId == null;
	}

	public static ReviewSearchCondition of(Address address, LocalDateTime referenceTime, Duration duration,
		ChronoUnit unit, Long tagId) {
		return new ReviewSearchCondition(address, referenceTime, duration, unit, tagId);
	}
}
