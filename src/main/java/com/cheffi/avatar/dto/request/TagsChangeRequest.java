package com.cheffi.avatar.dto.request;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TagsChangeRequest(

	@JsonProperty(value = "food_tags")
	@Schema(description = "변경할 음식 태그의 식별자 리스트 - 변경을 원하는 최종 상태를 3개 이상 입력해주세요.", example = "[1, 2, 3]")
	List<Long> foodTags,
	@JsonProperty(value = "taste_tags")
	@Schema(description = "변경할 취향 태그의 식별자 리스트 - 변경을 원하는 최종 상태를 5개 이상 입력해주세요.", example = "[20, 21, 22, 23, 24, 25]")
	List<Long> tasteTags) {

	public List<Long> combinedList() {
		List<Long> combined = new ArrayList<>();
		combined.addAll(foodTags);
		combined.addAll(tasteTags);
		return combined;
	}
}
