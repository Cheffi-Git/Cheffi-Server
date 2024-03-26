package com.cheffi.notice.dto;

import java.time.LocalDateTime;

import com.cheffi.notice.domain.Notice;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NoticeDto {

	@Schema(description = "제목")
	private final String title;

	@Schema(description = "내용")
	private final String content;

	@Schema(description = "공지 일시")
	private final LocalDateTime createdDate;

	public NoticeDto(Notice notice) {
		this.title = notice.getTitle();
		this.content = notice.getContent();
		this.createdDate = notice.getCreatedDate();
	}

}
