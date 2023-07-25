package com.cheffi.common.constant;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.MappedSuperclass;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Access(AccessType.FIELD)
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

	public boolean isSimpleAddress() {
		return this.getClass() == Address.class;
	}
}
