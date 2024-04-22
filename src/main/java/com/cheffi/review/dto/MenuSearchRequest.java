package com.cheffi.review.dto;

import org.hibernate.validator.constraints.Range;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class MenuSearchRequest {

	@Parameter(description = "검색 기준이 되는 메뉴명 [NotNull] [NotBlank]", example = "국밥")
	@NotBlank
	private final String menu;

	@Parameter(description = "검색을 시작할 커서(포함) 최초 조회시는 입력하지 말아주세요 [Nullable] [1 - ~]")
	@Nullable
	@Positive
	private final Long cursor;

	@Parameter(description = "검색 사이즈 [NotNull] [1 - 16]")
	@NotNull
	@Range(min = 1, max = 16)
	private final Integer size;

	public MenuSearchRequest(Long cursor, String menu, Integer size) {
		this.cursor = cursor;
		this.menu = menu.trim();
		this.size = size;
	}
}
