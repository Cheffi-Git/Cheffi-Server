package com.cheffi.avatar.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;

@Getter
public class FollowCursor {

	private final Long id;
	private final boolean followedByViewer;

	@QueryProjection
	public FollowCursor(Long id, boolean followedByViewer) {
		this.id = id;
		this.followedByViewer = followedByViewer;
	}
}
