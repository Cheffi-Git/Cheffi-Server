package com.cheffi.review.dto.request;

import java.util.List;

import com.cheffi.avatar.dto.request.TagsChangeRequest;
import com.cheffi.review.dto.MenuDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegisterReviewRequest {
	@Schema(description = "식당 식별자")
	private Long restaurantId;
	@NotNull
	@Schema(description = "등록된 식당 여부, false 인 경우 공공 데이터에서 식당을 찾아 등록합니다.")
	private boolean registered;
	@NotBlank
	@Schema(description = "리뷰 제목")
	private String title;
	@NotBlank
	@Schema(description = "리뷰 내용")
	private String text;
	@Schema(description = "메뉴")
	private List<MenuDto> menus;

	private TagsChangeRequest tag;
}
