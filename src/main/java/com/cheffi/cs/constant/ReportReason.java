package com.cheffi.cs.constant;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true,
	description = """
		* `INAPPROPRIATE` - 부적절한 사진 및 내용
		
		* `OFFENSIVE` - 욕설 및 비난
		
		* `ADVERTISING` - 광고성 게시물
		
		* `FRAUDULENT` - 사기 또는 거짓
		
		* `INFRINGEMENT` - 지적재산권 침해
		""")
public enum ReportReason {
	INAPPROPRIATE,
	OFFENSIVE,
	ADVERTISING,
	FRAUDULENT,
	INFRINGEMENT
}
