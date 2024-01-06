package com.cheffi.avatar.dto.common;

import com.cheffi.tag.constant.TagType;
import com.cheffi.tag.domain.Tag;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TagDto(
	@Schema(description = "태그의 식별자")
	Long id,
	@Schema(description = "태그의 이름")
	String name,
	@Schema(description = "태그의 타입")
	TagType type
) {

	public static TagDto of(Tag tag) {
		return new TagDto(tag.getId(), tag.getName(), tag.getTagType());
	}

}
