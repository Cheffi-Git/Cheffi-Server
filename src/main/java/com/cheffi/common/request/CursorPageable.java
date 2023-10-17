package com.cheffi.common.request;

import static org.apache.commons.lang3.StringUtils.*;

import java.time.LocalDateTime;
import java.util.Base64;

import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.BusinessException;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Positive;

public record CursorPageable(

	@Positive int size,
	String nextPageCursor
) {

	public boolean hasNextPageCursor() {
		return nextPageCursor != null && !nextPageCursor.isEmpty();
	}

	public String getDecodedCursor(String cursorValue) {

		if (cursorValue == null || cursorValue.isEmpty()) {
			throw new BusinessException(ErrorCode.NOT_VALID_CURSOR);
		}
		String decodedValue = new String(Base64.getDecoder().decode(cursorValue));

		return substringBetween(decodedValue, "###");
	}

	public String getEncodedCursor(Long field, boolean hasNextElements) {

		if (field == null) {
			throw new BusinessException(ErrorCode.NOT_VALID_CURSOR);
		}

		if (!hasNextElements) {
			return null;
		}

		String structuredValue = "###" + field + "### - " + LocalDateTime.now();

		return Base64.getEncoder().encodeToString(structuredValue.getBytes());

	}

	@JsonIgnore
	public String getCursorValue() {

		if (!hasNextPageCursor()) {
			return "";
		}

		return getDecodedCursor(nextPageCursor);
	}

	public CursorPageable(int size, String nextPageCursor) {

		if (size <= 0) {
			throw new BusinessException(ErrorCode.NOT_VALID_CURSOR);
		}

		this.size = size;
		this.nextPageCursor = nextPageCursor;
	}
}
