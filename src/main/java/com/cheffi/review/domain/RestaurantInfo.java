package com.cheffi.review.domain;

import com.cheffi.common.constant.DetailedAddress;

public interface RestaurantInfo {

	DetailedAddress getDetailedAddress();

	Long getId();

	boolean isRegistered();

	String getName();

}
