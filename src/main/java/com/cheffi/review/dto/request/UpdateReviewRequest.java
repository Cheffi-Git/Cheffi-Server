package com.cheffi.review.dto.request;

import java.util.List;

import com.cheffi.avatar.dto.request.TagsChangeRequest;
import com.cheffi.review.dto.MenuDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateReviewRequest {
	@NotNull
	@Schema(description = "수정할 리뷰 식별자", example = "1")
	private Long id;
	@NotBlank
	@Schema(description = "리뷰 제목", example = "기존 제목 or 수정할 제목")
	private String title;
	@NotBlank
	@Schema(description = "리뷰 내용", example = "기존 내용 or 수정할 내용")
	private String text;
	@Schema(description = "메뉴")
	private List<MenuDto> menus;

	private TagsChangeRequest tag;
}
