package com.cheffi.common.constant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Embeddable;
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
public class DetailedAddress extends Address {

	@Schema(description = "지번 주소", example = "염리동 111", nullable = true)
	@JsonIgnore
	private String lotNumber;

	@Schema(description = "도로명 주소", example = "숭문길 24")
	@NotBlank
	@NotNull
	private String roadName;

	public DetailedAddress(String province, String city, String lotNumber, String roadName) {
		super(province, city);
		this.lotNumber = lotNumber.trim().replaceAll("\\s+", " ");
		this.roadName = roadName.trim().replaceAll("\\s+", " ");
	}

	public static DetailedAddress of(String province, String city, String lotNumber, String roadName) {
		return new DetailedAddress(province, city, lotNumber, roadName);
	}

	@Schema(description = "식당 전체 지번 주소", example = "서울시 마포구 염리동 111")
	public String getFullLotNumberAddress() {
		return getProvince() + " " + getCity() + " " + getLotNumber();
	}

	@Schema(description = "식당 전체 도로명 주소", example = "서울시 마포구 숭문길 24")
	public String getFullRodNameAddress() {
		return getProvince() + " " + getCity() + " " + getRoadName();
	}

}
