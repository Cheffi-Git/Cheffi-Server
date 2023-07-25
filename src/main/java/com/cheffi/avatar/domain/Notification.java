package com.cheffi.avatar.domain;

import jakarta.persistence.Entity;
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
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String content;

	private boolean checked;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "avatar_id")
	private Avatar avatar;

	public Notification(String content, Avatar avatar) {
		this.content = content;
		this.avatar = avatar;
		this.checked = false;
	}
}
