package com.cheffi.review.dto;

import org.hibernate.validator.constraints.Range;

import com.cheffi.common.constant.Address;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class AddressSearchRequest {

	@Valid
	private final Address address;

	@Parameter(description = "검색을 시작할 커서(포함) 최초 조회시는 입력하지 말아주세요 [Nullable] [1 - ~]")
	@Nullable
	@Positive
	private final Long cursor;

	@Parameter(description = "검색 사이즈 [NotNull] [1 - 16]")
	@NotNull
	@Range(min = 1, max = 16)
	private final Integer size;

	public AddressSearchRequest(Long cursor, String province, String city, Integer size) {
		this.cursor = cursor;
		this.address = Address.cityAddress(province, city);
		this.size = size;
	}

}
