package com.cheffi.review.helper;

import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.cheffi.review.dto.ReviewSearchCondition;

@Service
public class ReviewRedisKeyGenerator {

	private final DateTimeFormatter hourFormatter;
	private final DateTimeFormatter monthFormatter;

	public ReviewRedisKeyGenerator() {
		this.hourFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH");
		this.monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	}

	public String getTrendingSearchKey(ReviewSearchCondition condition) {
		if (condition.noTagCondition())
			return condition.getAddress().getCombined() + condition.getReferenceTime().format(hourFormatter);

		return condition.getAddress().getCombined() + "tag_id:" + condition.getTagId() + " " + condition.getReferenceTime()
			.format(monthFormatter);
	}

}
