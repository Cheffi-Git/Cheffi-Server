package com.cheffi.region.service;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class RegionDto {
	private String province;
	private List<String> cities;

	public RegionDto(RegionProperties.Region region) {
		this.province = region.getProvince();
		this.cities = new ArrayList<>();
		this.cities.addAll(region.getCities());
	}
}
