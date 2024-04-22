package com.cheffi.avatar.dto.response;

import java.util.List;

import com.cheffi.avatar.dto.common.TagDto;
import com.cheffi.tag.constant.TagType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TagsChangeResponse(

	@Schema(description = "변경된 태그의 리스트", required = true)
	List<TagDto> tags,

	@Schema(description = "변경된 태그의 타입", required = true)
	TagType type
) {
}
