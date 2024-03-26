package com.cheffi.review.dto.request;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.hibernate.validator.constraints.Range;

import com.cheffi.common.constant.Address;
import com.cheffi.common.dto.RedisZSetRequest;
import com.cheffi.review.dto.ReviewSearchCondition;
import com.cheffi.util.constant.SearchConstant;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

@Getter
public class AreaSearchRequest implements RedisZSetRequest {

	@Valid
	private final Address address;

	@Parameter(description = "검색을 시작할 커서(포함) 최초 조회시는 입력하지 말아주세요 [Nullable]")
	@Nullable
	@PositiveOrZero
	private final Long cursor;

	@Parameter(description = "검색 사이즈")
	@NotNull
	@Range(min = 1, max = 16)
	private final Integer size;

	@JsonIgnore
	private final LocalDateTime referenceTime;

	public AreaSearchRequest(String province, String city, Long cursor, Integer size) {
		this.address = Address.cityAddress(province, city);
		this.cursor = cursor;
		this.size = size;
		this.referenceTime = LocalDateTime.now();
	}

	@Override
	public Long getStart() {
		return this.cursor != null ? this.cursor : 0;
	}

	public ReviewSearchCondition toSearchCondition() {
		return ReviewSearchCondition.of(this.address, this.referenceTime, SearchConstant.TRENDING_UPDATE_CYCLE,
			ChronoUnit.HOURS, null);
	}
}
