package com.cheffi.event.event;

import com.cheffi.avatar.domain.Avatar;
import com.cheffi.common.auth.Authorities;
import com.cheffi.review.domain.Review;

import lombok.Getter;

@Getter
public class ReviewCreateEvent {

	private final Long writerId;
	private final String nickname;
	private final String title;
	private final Authorities authorities;

	public ReviewCreateEvent(Avatar writer, Review review, Authorities authorities) {
		this.writerId = writer.getId();
		this.nickname = writer.getNickname();
		this.title = review.getTitle();
		this.authorities = authorities;
	}

}
