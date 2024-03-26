package com.cheffi.review.helper;

import org.springframework.stereotype.Service;

import com.cheffi.review.dto.ReviewSearchCondition;
import com.cheffi.util.model.ExactPeriod;

@Service
public class ReviewSearchPeriodStrategy {

	public ExactPeriod getSearchPeriod(ReviewSearchCondition condition) {
		return new ExactPeriod(condition.getReferenceTime(), condition.getSearchPeriod(), condition.getUnit());
	}

}
