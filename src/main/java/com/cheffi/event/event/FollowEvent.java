package com.cheffi.event.event;

import com.cheffi.avatar.domain.Avatar;

import lombok.Getter;

@Getter
public class FollowEvent {

	private final Long targetId;
	private final String subjectNickname;

	public FollowEvent(Avatar target, Avatar subject) {
		this.targetId = target.getId();
		this.subjectNickname = subject.stringNickname();
	}
}
