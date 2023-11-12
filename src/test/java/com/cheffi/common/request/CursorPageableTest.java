package com.cheffi.common.request;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CursorPageableTest {

	@Test
	@DisplayName("success - Cursor의 field값이 Long타입인 경우의 인코딩, 디코딩 통합 테스트")
	public void testEncoding() {

		final int SIZE = 5;
		final Long FIELD = 2L;

		CursorPageable cursorPageable = new CursorPageable(SIZE, "");

		String encodedCursor = cursorPageable.getEncodedCursor(FIELD, true);
		String decodedCursor = cursorPageable.getDecodedCursor(encodedCursor);

		assertEquals(Long.valueOf(decodedCursor), FIELD);

	}
}


