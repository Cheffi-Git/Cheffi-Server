package com.cheffi.common.constant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Access(AccessType.FIELD)
@Embeddable
@MappedSuperclass
public class Address {

	// 시, 도
	@NotBlank
	@Parameter(name = "province", description = "검색 기준 시/도 주소(1차)", example = "서울특별시")
	@Schema(description = "식당의 시/도 주소(1차)", example = "서울특별시")
	@NotNull
	private String province;

	// 구, 시, 군
	@NotBlank
	@Parameter(name = "city", description = "검색 기준 시/군/구 주소(2차)", example = "양천구")
	@Schema(description = "식당의 시/군/구 주소(2차)", example = "양천구")
	@NotNull
	private String city;

	protected Address(String province, String city) {
		this.province = province;
		this.city = city;
	}

	public String getCombined() {
		return getProvince() + " " + getCity();
	}

	public static Address cityAddress(String province, String city) {
		return new Address(province, city);
	}

	@JsonIgnore
	public boolean isSimpleAddress() {
		return this.getClass() == Address.class;
	}
}
