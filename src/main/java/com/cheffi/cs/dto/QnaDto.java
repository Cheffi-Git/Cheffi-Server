package com.cheffi.cs.dto;

import com.cheffi.cs.constant.QnaCategory;
import com.cheffi.cs.domain.Qna;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class QnaDto {

	@Schema(description = "질문", required = true)
	private final String question;
	@Schema(description = "답변", required = true)
	private final String answer;
	@Schema(required = true) private final QnaCategory category;
	@Schema(description = "카테고리명", example = "이용문의", required = true)
	private final String categoryName;

	public QnaDto(String question, String answer, QnaCategory category) {
		this.question = question;
		this.answer = answer;
		this.category = category;
		this.categoryName = category.getTitle();
	}

	public static QnaDto of(Qna qna) {
		return new QnaDto(qna.getQuestion(), qna.getAnswer(), qna.getCategory());
	}
}
