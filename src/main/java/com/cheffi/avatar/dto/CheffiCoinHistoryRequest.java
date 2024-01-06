package com.cheffi.avatar.dto;

import org.hibernate.validator.constraints.Range;

import com.cheffi.avatar.constant.CfcHistoryCategory;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

@Getter
public class CheffiCoinHistoryRequest {

	@Parameter(description = "검색을 시작할 커서(포함) 최초 조회시는 0을 넣어 주세요", example = "0")
	@NotNull
	@PositiveOrZero
	private Long cursor;

	@Parameter(description = "검색 사이즈", example = "10")
	@NotNull
	@Range(min = 1, max = 16)
	private Integer size;

	@Parameter(description = "충전만 조회할 지 차감만 조회할 지 선택, 전체 조회는 입력을 안하면 됩니다.", example = "PLUS")
	@Nullable
	private CfcHistoryCategory category;

	public CheffiCoinHistoryRequest(Long cursor, Integer size, CfcHistoryCategory category) {
		this.cursor = cursor;
		this.size = size;
		this.category = category;
	}
}
