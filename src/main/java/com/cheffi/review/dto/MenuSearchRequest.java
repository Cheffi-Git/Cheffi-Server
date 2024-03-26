package com.cheffi.review.dto;

import org.hibernate.validator.constraints.Range;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MenuSearchRequest {
	@Parameter(description = "검색 기준이 되는 메뉴명", example = "국밥")
	@NotBlank
	private final String menu;

	@Valid
	private final ReviewCursor cursor;

	@Parameter(description = "검색 사이즈")
	@NotNull
	@Range(min = 1, max = 16)
	private final Integer size;

	public MenuSearchRequest(Long id, Integer count, String menu, Integer size) {
		this.cursor = new ReviewCursor(id, count);
		this.menu = menu.trim();
		this.size = size;
	}
}
