package com.cheffi.common.constant;

import jakarta.persistence.Embeddable;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
@MappedSuperclass
public class Address {

	// 시, 도
	@NotNull
	private String province;

	// 구, 시, 군
	@NotNull
	private String city;

	// 동, 읍, 면, 리


	protected Address(String province, String city) {
		this.province = province;
		this.city = city;
	}


	public static Address cityAddress(String province, String city) {
		return new Address(province, city);
	}
}
