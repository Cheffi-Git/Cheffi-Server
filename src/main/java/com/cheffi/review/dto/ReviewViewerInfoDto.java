package com.cheffi.review.dto;

import com.cheffi.review.constant.RatingType;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReviewViewerInfoDto {
	private final boolean bookmarked;
	private final Long matchedTagNum;
	private final boolean ratedByUser;
	private final RatingType ratingType;

	@Builder
	private ReviewViewerInfoDto(boolean bookmarked, Long matchedTagNum, boolean ratedByUser, RatingType ratingType) {
		this.bookmarked = bookmarked;
		this.matchedTagNum = matchedTagNum;
		this.ratedByUser = ratedByUser;
		this.ratingType = ratingType;
	}
}
