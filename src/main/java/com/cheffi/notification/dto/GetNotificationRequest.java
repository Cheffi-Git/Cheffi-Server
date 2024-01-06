package com.cheffi.notification.dto;

import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetNotificationRequest {

	@Parameter(description = "검색을 시작할 커서의 id(포함) 최초 조회시에는 입력하지 말아주세요")
	@Nullable
	@PositiveOrZero
	private final Long cursor;

	@Parameter(description = "검색 사이즈")
	@NotNull
	@Range(min = 1, max = 16)
	private final Integer size;

	public GetNotificationRequest(Long cursor, Integer size) {
		this.cursor = cursor;
		this.size = size;
	}

	public Long getCursor(Long defaultValue) {
		return cursor != null ? cursor : defaultValue;
	}

}
