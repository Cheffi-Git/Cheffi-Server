package com.cheffi.user.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ChangeTermsAgreementRequest(
	@Schema(description = "마케팅 정보 수신 동의 여부", example = "true")
	@NotNull
	Boolean adAgreed
) {

}
