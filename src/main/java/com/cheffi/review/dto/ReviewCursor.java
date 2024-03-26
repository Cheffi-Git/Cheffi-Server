package com.cheffi.review.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.PositiveOrZero;

public class ReviewCursor {

	@Parameter(name = "id", description = "검색을 시작할 커서의 id(포함) 최초 조회시에는 입력하지 말아주세요")
	@Nullable
	@PositiveOrZero
	private final Long id;

	@Parameter(name = "count", description = "검색을 시작할 커서의 count (포함) 최초 조회시에는 입력하지 말아주세요")
	@Nullable
	@PositiveOrZero
	private final Integer count;

	@JsonIgnore
	@AssertTrue(message = "모든 값이 전부 입력되거나 입력되지 않아야 합니다.")
	public boolean isValidCursor() {
		if (this.id == null && this.count == null)
			return true;
		return this.id != null && this.count != null;
	}

	public ReviewCursor(Long id, Integer count) {
		this.id = id;
		this.count = count;
	}

	public Long getId() {
		return id == null ? Long.MAX_VALUE : id;
	}

	public Integer getCount() {
		return count == null ? Integer.MAX_VALUE : count;
	}

	public static ReviewCursor of(ReviewInfoDto reviewInfoDto) {
		return new ReviewCursor(reviewInfoDto.getId(), reviewInfoDto.getViewCount());
	}
}
