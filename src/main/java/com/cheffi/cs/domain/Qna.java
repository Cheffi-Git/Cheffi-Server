package com.cheffi.cs.domain;

import com.cheffi.cs.constant.QnaCategory;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Qna {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String question;

	@NotNull
	private String answer;

	@NotNull
	@Enumerated(EnumType.STRING)
	private QnaCategory category;

	@NotNull
	private boolean best;

	public Qna(String question, String answer, QnaCategory category) {
		this.question = question;
		this.answer = answer;
		this.category = category;
		this.best = false;
	}
}
