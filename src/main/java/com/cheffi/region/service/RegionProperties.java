package com.cheffi.region.service;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.cheffi.common.constant.Address;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "env")
class RegionProperties {

	private List<Region> regions;

	@Getter
	@Setter
	static class Region {
		private String province;
		private List<String> cities;

		private boolean match(Address address) {
			return province.equals(address.getProvince()) && cities.contains(address.getCity());
		}
	}

	boolean contains(Address address) {
		return regions.stream().anyMatch(region -> region.match(address));
	}
}
