package com.cheffi.notification.dto;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.Getter;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DeleteNotificationRequest {

	@Schema(description = "삭제할 알림의 id 목록")
	@Nullable
	private final List<Long> notifications;

	@Schema(description = "true: 전체 삭제, false: 선택 삭제(default)", example = "false")
	private final boolean deleteAll;

	public DeleteNotificationRequest(List<Long> notifications, boolean deleteAll) {
		this.notifications = notifications;
		this.deleteAll = deleteAll;
	}
}
