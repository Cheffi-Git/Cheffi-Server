package com.cheffi.cs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DeleteBlockRequest(
	@NotNull @Positive
	@Schema(description = "차단 해제 대상의 ID", example = "10")
	Long id
) {
}
