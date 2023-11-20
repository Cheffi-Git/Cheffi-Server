package com.cheffi.review.dto.request;

import java.util.List;

import com.cheffi.avatar.dto.request.TagsChangeRequest;
import com.cheffi.review.dto.MenuDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateReviewRequest {
	@Schema(description = "리뷰 식별자")
	private Long reviewId;
	@NotBlank
	@Schema(description = "리뷰 제목")
	private String title;
	@NotBlank
	@Schema(description = "리뷰 내용")
	private String text;
	@Schema(description = "메뉴")
	private List<MenuDto> menus;

	private TagsChangeRequest tag;
}
