package com.cheffi.tag.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cheffi.avatar.dto.common.TagDto;
import com.cheffi.common.response.ApiResponse;
import com.cheffi.tag.constant.TagType;
import com.cheffi.tag.service.TagService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/tags")
public class TagController {

	private final TagService tagService;

	@Tag(name = "Tags")
	@Operation(summary = "모든 태그 조회 API")
	@GetMapping
	public ApiResponse<List<TagDto>> getMyFollowee(@RequestParam @Nullable TagType type) {
		return ApiResponse.success(tagService.getAllTags(type));
	}

}
