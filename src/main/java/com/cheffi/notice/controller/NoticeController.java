package com.cheffi.notice.controller;

import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cheffi.common.response.ApiPageResponse;
import com.cheffi.notice.dto.NoticeDto;
import com.cheffi.notice.service.NoticeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/notice")
public class NoticeController {

	private final NoticeService noticeService;

	@Tag(name = "${swagger.tag.notice}")
	@Operation(summary = "공지 사항 조회 API",
		description = "알림 목록 조회 API - 인증 필수, 커서 페이징, 해당 API 로 조회된 알림은 자동으로 확인처리됩니다.")
	@PageableAsQueryParam
	@GetMapping
	public ApiPageResponse<NoticeDto> notifications(
		@Parameter(hidden = true)
		@PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
		return ApiPageResponse.success(noticeService.getNotices(pageable));
	}

}
