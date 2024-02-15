package com.cheffi.health.controller;

import java.util.Arrays;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cheffi.health.dto.response.HealthCheckResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Health check", description = "서버 상태 체크 API")
@RequiredArgsConstructor
@RestController
public class HealthCheckController {
	private final Environment environment;

	@Tag(name = "Health check")
	@GetMapping("/health")
	@Operation(summary = "서버 Health Check API", description = "현재 서버가 정상적으로 기동이 된 상태인지 검사하는 API")
	public HealthCheckResponse healthCheck() {
		return HealthCheckResponse.of("서버가 정상적인 상태입니다.",
			Arrays.asList(environment.getActiveProfiles()));
	}

}
