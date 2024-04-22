package com.cheffi.notification.domain;

import com.cheffi.notification.constant.DevicePlatform;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;

@Getter
public class PushNotification {

	private final String body;
	private final String title;
	private final String token;
	private final DevicePlatform platform;

	@QueryProjection
	public PushNotification(Notification notification, Device device) {
		this.body = notification.getBody();
		this.title = notification.getTitle();
		this.token = device.getDeviceToken();
		this.platform = device.getPlatform();
	}

	public PushNotification(String body, String title, String token, DevicePlatform platform) {
		this.body = body;
		this.title = title;
		this.token = token;
		this.platform = platform;
	}
}
