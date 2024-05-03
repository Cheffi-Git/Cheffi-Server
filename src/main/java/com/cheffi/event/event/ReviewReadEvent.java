package com.cheffi.event.event;

import java.io.Serializable;

import org.springframework.util.Assert;

import com.cheffi.review.domain.Review;

import lombok.Getter;

@Getter
public class ReviewReadEvent implements Serializable {

	private final Long reviewId;
	private final Long viewerId;
	private final boolean authenticated;

	public ReviewReadEvent(Review review) {
		Assert.notNull(review, "ReadReviewEvent의 생성자에는 review가 Null일 수 없습니다.");
		this.reviewId = review.getId();
		this.viewerId = null;
		this.authenticated = false;
	}

	public ReviewReadEvent(Review review, Long viewerId) {
		Assert.notNull(review, "ReadReviewEvent의 생성자에는 review가 Null일 수 없습니다.");
		Assert.notNull(viewerId, "ReadReviewEvent의 생성자에는 viewerId가 Null일 수 없습니다.");
		this.reviewId = review.getId();
		this.viewerId = viewerId;
		this.authenticated = true;
	}

}
