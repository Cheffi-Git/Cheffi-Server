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
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

@Getter
public class AreaTagSearchRequest implements RedisZSetRequest {

	@Valid
	private final Address address;

	@Parameter(description = "검색을 시작할 커서(포함) 최초 조회시는 0을 넣어주세요")
	@NotNull
	@PositiveOrZero
	private final Long cursor;

	@Parameter(description = "검색 사이즈")
	@NotNull
	@Range(min = 1, max = 16)
	private final Integer size;

	@Parameter(name = "tag_id", description = "검색할 태그의 인덱스, 음식 태그만 입력 가능합니다.", example = "15")
	@NotNull
	private final Long tagId;
	@JsonIgnore
	private final LocalDateTime referenceTime;

	public AreaTagSearchRequest(String province, String city, Long cursor, Integer size, Long tag_id) {
		this.address = Address.cityAddress(province, city);
		this.cursor = cursor;
		this.size = size;
		this.tagId = tag_id;
		this.referenceTime = LocalDateTime.now();
	}

	@Override
	public Long getStart() {
		return this.cursor;
	}

	public ReviewSearchCondition toSearchCondition() {
		return ReviewSearchCondition.of(this.address, this.referenceTime, SearchConstant.TAG_UPDATE_CYCLE,
			ChronoUnit.DAYS, this.tagId);
	}
}
