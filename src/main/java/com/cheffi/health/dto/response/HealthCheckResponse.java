package com.cheffi.health.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class HealthCheckResponse {

	@Schema(description = "서버 health 상태", example = "ok")
	private String health;

	@Schema(description = "현재 실행 중인 profile", example = "[prod]")
	private List<String> activeProfiles;

	@Schema(description = "현재 요청 서버 시간", example = "2023-01-23 16:31:04")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private LocalDateTime currentTimeStamp;

	public static HealthCheckResponse of(String message, List<String> activeProfiles) {
		return HealthCheckResponse.builder()
			.health(message)
			.activeProfiles(activeProfiles)
			.currentTimeStamp(LocalDateTime.now())
			.build();
	}

}
