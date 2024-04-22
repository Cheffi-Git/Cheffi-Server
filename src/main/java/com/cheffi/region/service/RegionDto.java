package com.cheffi.region.service;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class RegionDto {
	@Schema(required = true) private String province;
	@Schema(required = true) private List<String> cities;

	public RegionDto(RegionProperties.Region region) {
		this.province = region.getProvince();
		this.cities = new ArrayList<>();
		this.cities.addAll(region.getCities());
	}
}
