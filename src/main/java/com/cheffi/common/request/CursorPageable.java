package com.cheffi.common.request;

import static org.apache.commons.lang3.StringUtils.*;

import java.time.LocalDateTime;
import java.util.Base64;

import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.BusinessException;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Positive;

public record CursorPageable(

	int size,
	String nextPageCursor
) {

	private static final int SIZE_MIN = 5;
	private static final int FOR_CHECK_CURSOR = 1;

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

		if (size < SIZE_MIN) {
			throw new BusinessException(ErrorCode.NOT_VALID_CURSOR_SIZE);
		}

		this.size = size + FOR_CHECK_CURSOR;
		this.nextPageCursor = nextPageCursor;
	}
}
