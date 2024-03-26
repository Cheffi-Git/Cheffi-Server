package com.cheffi.cs.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cheffi.common.response.ApiResponse;
import com.cheffi.cs.constant.QnaCategory;
import com.cheffi.cs.dto.QnaDto;
import com.cheffi.cs.service.QnaService;
import com.cheffi.oauth.model.UserPrincipal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/qna")
public class QnaController {

	private final QnaService qnaService;

	@Tag(name = "${swagger.tag.qna}")
	@Operation(summary = "자주 묻는 질문 API",
		description = "카테고리별로 자주 묻는 질문을 조회하는 API"
	)
	@GetMapping
	public ApiResponse<List<QnaDto>> addBlock(
		@AuthenticationPrincipal UserPrincipal principal,
		@NotNull QnaCategory category) {
		return ApiResponse.success(qnaService.getByCategory(category));
	}

}
