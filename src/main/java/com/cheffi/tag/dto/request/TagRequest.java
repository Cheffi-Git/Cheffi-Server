package com.cheffi.tag.dto.request;

import com.cheffi.tag.constant.TagType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class TagRequest {
	@Schema(description = "태그의 식별자", example = "1")
	private Long id;
	@Schema(description = "태그의 타입", example = "FOOD")
	private TagType type;
}
