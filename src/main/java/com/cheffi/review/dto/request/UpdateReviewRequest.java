package com.cheffi.review.dto.request;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.cheffi.review.dto.MenuDto;
import com.cheffi.tag.constant.TagType;
import com.cheffi.tag.dto.request.TagRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateReviewRequest {
	@Schema(description = "수정할 리뷰 식별자", example = "1")
	@NotNull
	private Long id;
	@Schema(description = "리뷰 제목", example = "기존 제목 or 수정할 제목")
	@NotBlank
	private String title;
	@Schema(description = "리뷰 내용", example = "기존 내용 or 수정할 내용")
	@NotBlank
	private String text;
	@Schema(description = "메뉴")
	private List<MenuDto> menus;
	@JsonIgnore
	private Map<TagType, List<Long>> map;
	@Schema(description = "추가할 태그의 식별자, 타입의 리스트, 음식 태그는 최소 1개, 취향 태그는 최소 2개 이상 입력해야 합니다.")
	private List<TagRequest> tags;

	public UpdateReviewRequest(Long id, String title, String text, List<MenuDto> menus, List<TagRequest> tags) {
		this.id = id;
		this.title = title;
		this.text = text;
		this.menus = menus;
		this.tags = tags;
		this.map = new EnumMap<>(TagType.class);
		for (TagRequest tag : tags) {
			List<Long> list = map.getOrDefault(tag.getType(), new ArrayList<>());
			list.add(tag.getId());
			map.put(tag.getType(), list);
		}
	}
}
