package com.cheffi.review.constant;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true,
	description = """
		* `ACTIVE` - 리뷰 활성 상태
				
		* `DELETED` - 리뷰 삭제 상태
				
		* `BANNED` - 리뷰 강제 조치 상태
		""")
public enum ReviewStatus {

	ACTIVE, DELETED, BANNED

}
