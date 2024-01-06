package com.cheffi.avatar.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.Size;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PatchNicknameRequest(
	@Size(min = 2, max = 8)
	String nickname
) {
}
