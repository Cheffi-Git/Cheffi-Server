package com.cheffi.common.response;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ErrorResponse {

	private String errorCode;
	private String errorMessage;
	private Map<String, ?> data;

	public static ErrorResponse of(String errorCode, String errorMessage) {
		return ErrorResponse.builder().errorCode(errorCode).errorMessage(errorMessage).build();
	}

	public static ErrorResponse of(String errorCode, String errorMessage, Map<String, ?> fields) {
		return ErrorResponse.builder().errorCode(errorCode).errorMessage(errorMessage).data(fields).build();
	}

	public static ErrorResponse of(String errorCode, BindingResult bindingResult) {
		return ErrorResponse.builder()
			.errorCode(errorCode)
			.errorMessage(createErrorMessage(bindingResult))
			.data(bindingResult.getFieldErrors()
				.stream()
				.collect(
					Collectors.toMap(FieldError::getField, f -> Optional.ofNullable(f.getDefaultMessage()).orElse(""))))
			.build();
	}

	private static String createErrorMessage(BindingResult bindingResult) {
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		List<FieldError> fieldErrors = bindingResult.getFieldErrors();
		for (FieldError fieldError : fieldErrors) {
			if (!isFirst) {
				sb.append(", ");
			} else {
				isFirst = false;
			}
			sb.append("[");
			sb.append(fieldError.getField());
			sb.append("] ");
			sb.append(fieldError.getDefaultMessage());
		}

		return sb.toString();
	}

}
