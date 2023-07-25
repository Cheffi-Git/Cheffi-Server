package com.cheffi.avatar.domain;

import java.time.LocalDateTime;

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
public class Inquiry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	private String title;
	@NotNull
	private String content;
	private String answer;
	private boolean answered;
	private LocalDateTime answeredDate;
	@ManyToOne
	@JoinColumn(name = "inquirer_id")
	private Avatar avatar;

	private Inquiry(String title, String content, Avatar avatar) {
		this.title = title;
		this.content = content;
		this.answered = false;
		this.avatar = avatar;
	}

	public static Inquiry of(String title, String content, Avatar avatar) {
		return new Inquiry(title, content, avatar);
	}

	public void answer(String answer) {
		this.answer = answer;
		this.answered = true;
		this.answeredDate = LocalDateTime.now();
	}
}
