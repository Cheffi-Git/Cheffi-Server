package com.cheffi.region.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cheffi.common.constant.Address;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RegionService {

	private final RegionProperties regionProperties;

	public boolean contains(Address address) {
		return regionProperties.contains(address);
	}

	public List<RegionDto> getRegion() {
		List<RegionProperties.Region> regions = regionProperties.getRegions();
		return regions.stream().map(RegionDto::new).toList();
	}
}
