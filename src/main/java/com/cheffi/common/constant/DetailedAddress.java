package com.cheffi.common.constant;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class DetailedAddress extends Address{

	@NotNull
	private String town;
	@NotNull
	private String detail;

	protected DetailedAddress(String province, String city, String town, String detail) {
		super(province, city);
		this.town = town;
		this.detail = detail;
	}

	public static DetailedAddress of(String province, String city, String town, String detail) {
		return new DetailedAddress(province, city, town, detail);
	}
}
