package com.cheffi.avatar.dto;

import java.time.LocalDateTime;

import com.cheffi.avatar.domain.CheffiCoin;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CheffiCoinHistoryDto {

	@Schema(description = "커서(인덱스)", example = "10")
	private final Long id;
	@Schema(description = "값", example = "-1")
	private final Integer value;
	@Schema(description = "설명", example = "12월 25일 접속 보상")
	private final String description;
	@Schema(description = "내역 생성 일자", example = "2023-11-30T21:35:08.532602")
	private final LocalDateTime createdDate;

	private CheffiCoinHistoryDto(Long id, Integer value, String description, LocalDateTime createdDate) {
		this.id = id;
		this.value = value;
		this.description = description;
		this.createdDate = createdDate;
	}

	public static CheffiCoinHistoryDto of(CheffiCoin cfc) {
		return new CheffiCoinHistoryDto(cfc.getId(), cfc.getCfcValue(), cfc.getDescription(), cfc.getCreatedDate());
	}
}
