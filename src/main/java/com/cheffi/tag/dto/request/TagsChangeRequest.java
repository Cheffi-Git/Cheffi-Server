package com.cheffi.tag.dto.request;

import java.util.List;

import com.cheffi.tag.constant.TagType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TagsChangeRequest(

	@JsonProperty(value = "ids")
	@Schema(description = "변경할 태그의 식별자 리스트", example = "[1, 2, 3]")
	List<Long> ids,

	@JsonProperty(value = "type")
	@Schema(description = "변경할 태그의 타입", example = "FOOD")
	TagType type

) {
}
