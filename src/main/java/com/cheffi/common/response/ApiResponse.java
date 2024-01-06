package com.cheffi.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ApiResponse<T> {

	private final T data;

	@Schema(description = "응답 코드", example = "200")
	private final int code;
	@Schema(description = "응답 메세지", example = "success")
	private final String message;

	private ApiResponse(T data, int code, String message) {
		this.data = data;
		this.code = code;
		this.message = message;
	}

	public static <T> ApiResponse<T> success(T data) {
		return new ApiResponse<>(data, 200, "success");
	}

	public static ApiResponse<Void> success() {
		return new ApiResponse<>(null, 200, "success");
	}
}
