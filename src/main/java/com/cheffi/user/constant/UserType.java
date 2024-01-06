package com.cheffi.user.constant;

public enum UserType {

	LOCAL, KAKAO, APPLE;

	public static UserType from(String provider) {
		return UserType.valueOf(provider.toUpperCase());
	}

}
