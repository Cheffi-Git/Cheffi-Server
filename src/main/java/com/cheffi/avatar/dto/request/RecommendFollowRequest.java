package com.cheffi.avatar.dto.request;

import static com.cheffi.common.dto.ValidationGroups.*;

import java.util.List;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class RecommendFollowRequest {

	@NotEmpty(groups = NotEmptyGroup.class)
	List<Long> tags;

	public RecommendFollowRequest(List<Long> tags) {
		this.tags = tags;
	}

	/**
	 * 메서드명이 is로 시작해야 Validator 가 인식 가능하다.
	 */
	@AssertTrue(message = "모든 태그 ID는 1 이상이어야 합니다.", groups = AfterEmptyCheckGroup.class)
	public boolean isPositive() {
		return tags != null && tags.stream().allMatch(i -> i > 0);
	}

}
