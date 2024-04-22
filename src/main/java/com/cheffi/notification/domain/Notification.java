package com.cheffi.notification.domain;

import com.cheffi.avatar.domain.Avatar;
import com.cheffi.common.domain.BaseEntity;
import com.cheffi.notification.constant.NotificationCategory;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Notification extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String body;

	@NotNull
	private String title;

	@NotNull
	@Enumerated(EnumType.STRING)
	private NotificationCategory category;

	private boolean checked;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "avatar_id")
	private Avatar target;

	private Notification(NotificationCategory category, Avatar target, Object... arg) {
		this.body = category.getBody(arg);
		this.title = category.getTitle(arg);
		this.category = category;
		this.target = target;
		this.checked = false;
	}

	public static Notification ofReview(Avatar target, String nickname) {
		return new Notification(NotificationCategory.REVIEW, target, nickname);
	}

	public static Notification ofOfficial(Avatar target, String title) {
		return new Notification(NotificationCategory.OFFICIAL, target, title);
	}

	public static Notification ofFollow(Avatar target, String nickname) {
		return new Notification(NotificationCategory.FOLLOW, target, nickname);
	}

	public static Notification ofBookmark(Avatar target, String title) {
		return new Notification(NotificationCategory.BOOKMARK, target, title);
	}

}
