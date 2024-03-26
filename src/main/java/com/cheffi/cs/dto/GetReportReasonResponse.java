package com.cheffi.cs.dto;

import com.cheffi.cs.constant.ReportReason;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class GetReportReasonResponse {

	@Schema(description = "사유 영문 문자열(요청에 쓰임)", example = "INAPPROPRIATE", required = true)
	private final ReportReason reason;

	public GetReportReasonResponse(ReportReason reason) {
		this.reason = reason;
	}

	@Schema(description = "한글 텍스트 (표시용 데이터)", example = "부적절한 사진 및 내용", required = true)
	@JsonInclude
	public String getText() {
		return this.reason.getText();
	}
}
