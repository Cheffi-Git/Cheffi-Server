package com.cheffi.notification.constant;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true,
	description = """
		* `REVIEW` - 리뷰 관련 알림
				
		* `BOOKMARK` - 찜 관련 알림
				
		* `FOLLOW` - 팔로우 관련 알림
				
		* `OFFICIAL` - 공식 게시글 알림
		""")
public enum NotificationCategory {
	REVIEW("'%s'님께서 새로운 게시글을 등록했어요", "새로운 리뷰가 등록됐어요"),
	BOOKMARK("'%s'의 글이 유료전환까지 1시간 남았어요", "찜하신 리뷰가 곧 잠겨요\uD83D\uDEA8"),
	FOLLOW("'%s'님께서 나를 팔로우 했어요", "팔로우 알림"),
	OFFICIAL("'%s' 게시글이 등록되었어요", "공식 게시글");

	private final String bodyFormat;
	private final String titleFormat;

	NotificationCategory(String bodyFormat, String titleFormat) {
		this.bodyFormat = bodyFormat;
		this.titleFormat = titleFormat;
	}

	public String getBody(Object... arg) {
		return String.format(this.bodyFormat, arg);
	}

	public String getTitle(Object... arg) {
		return String.format(this.titleFormat, arg);
	}

}
