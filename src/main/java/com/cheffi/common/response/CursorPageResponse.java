package com.cheffi.common.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;

@ToString
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
public class CursorPageResponse<T>{

	@Schema(description = "데이터")
	private final T data;
	@Schema(description = "다음 데이터의 시작점이 되는 cursor", example = "Null or IyMjNCMjIyAtIDIwMjMtMTAtMDVUMTg6MDM6MDYuNjAzNTI1")
	private final String nextPageCursor;
	@Schema(description = "응답 코드", example = "200")
	private final int code;
	@Schema(description = "응답 메세지", example = "success")
	private final String message;

	public CursorPageResponse(T data, String nextPageCursor, int code, String message) {
		this.data = data;
		this.nextPageCursor = nextPageCursor;
		this.code = code;
		this.message = message;
	}

	public static <T> CursorPageResponse<T> success(T data, String nextPageCursor) {

		return new CursorPageResponse<>(data, nextPageCursor, 200, "success");
	}
}
