package com.cheffi.review.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScoreDto {

	private Long id;
	private int count;

	@QueryProjection
	public ScoreDto(Long id, int count) {
		this.id = id;
		this.count = count;
	}
}
