package com.cheffi.review.dto;

import org.hibernate.validator.constraints.Range;

import com.cheffi.common.constant.Address;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AddressSearchRequest {

	@Valid
	private final Address address;

	@Valid
	private final ReviewCursor cursor;

	@Parameter(description = "검색 사이즈")
	@NotNull
	@Range(min = 1, max = 16)
	private final Integer size;

	public AddressSearchRequest(Long id, Integer count, String province, String city, Integer size) {
		this.cursor = new ReviewCursor(id, count);
		this.address = Address.cityAddress(province, city);
		this.size = size;
	}

}
