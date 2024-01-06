package com.cheffi.common.config.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cheffi.common.config.exception.business.AuthenticationException;
import com.cheffi.common.config.exception.business.BusinessException;
import com.cheffi.common.config.exception.business.DuplicatedEmailException;
import com.cheffi.common.response.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * jakarta.validation.Valid 또는 @Validated binding error가 발생할 경우
	 */
	@ExceptionHandler(BindException.class)
	protected ResponseEntity<ErrorResponse> handleBindException(BindException e) {
		log.error("handleBindException", e);
		ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST.toString(), e.getBindingResult());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(errorResponse);
	}

	/**
	 * 이메일 중복 오류 발생
	 */
	@ExceptionHandler(value = {DuplicatedEmailException.class})
	protected ResponseEntity<ErrorResponse> handleDuplicatedEmailException(DuplicatedEmailException e) {
		log.error("DuplicatedEmailException", e);
		ErrorResponse errorResponse = ErrorResponse.of(e.getErrorCode().getCode(), e.getMessage(),
			Map.of("provider", e.getProvider()));
		return ResponseEntity.status(e.getErrorCode().getHttpStatus())
			.body(errorResponse);
	}

	/**
	 * 인증 오류 발생
	 */
	@ExceptionHandler(value = {AuthenticationException.class})
	protected ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException e) {
		log.error("AuthenticationException", e.getOriginalException());
		ErrorResponse errorResponse = ErrorResponse.of(e.getErrorCode().getCode(), e.getMessage());
		return ResponseEntity.status(e.getErrorCode().getHttpStatus())
			.body(errorResponse);
	}

	/**
	 * 비즈니스 로직 실행 중 오류 발생
	 */
	@ExceptionHandler(value = {BusinessException.class})
	protected ResponseEntity<ErrorResponse> handleConflict(BusinessException e) {
		log.error("BusinessException", e);
		ErrorResponse errorResponse = ErrorResponse.of(e.getErrorCode().getCode(), e.getMessage());
		return ResponseEntity.status(e.getErrorCode().getHttpStatus())
			.body(errorResponse);
	}

	/**
	 * 나머지 예외 발생
	 */
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponse> handleException(Exception e) {
		log.error("Exception", e);
		ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}
}
