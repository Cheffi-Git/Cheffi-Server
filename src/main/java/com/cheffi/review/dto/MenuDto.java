package com.cheffi.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record MenuDto(
	@Schema(description = "메뉴 이름", example = "곱창 전골")
	String name,
	@Schema(description = "메뉴 가격", example = "80000")
	int price,
	@Schema(description = "메뉴 설명, 입력하지 않아도 됩니다.")
	String description) {
}
