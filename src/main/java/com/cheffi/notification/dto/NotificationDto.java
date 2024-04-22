package com.cheffi.notification.dto;

import java.time.LocalDateTime;

import com.cheffi.notification.constant.NotificationCategory;
import com.cheffi.notification.domain.Notification;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record NotificationDto(
	@Schema(description = "알림 ID", example = "10")
	Long id,
	@Schema(description = "알림 내용", example = "'김쉐피'님께서 새로운 게시글을 등록했어요")
	String content,
	@Schema(description = "알림 종류", example = "REVIEW")
	NotificationCategory category,
	@Schema(description = "알림 확인 여부", example = "true")
	Boolean checked,
	@Schema(description = "알림 시각")
	LocalDateTime notifiedDate
) {

	public static NotificationDto of(Notification no) {
		return new NotificationDto(no.getId(), no.getBody(), no.getCategory(), no.isChecked(), no.getCreatedDate());
	}

}
