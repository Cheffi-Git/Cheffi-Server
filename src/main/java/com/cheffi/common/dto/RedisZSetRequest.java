package com.cheffi.common.dto;

public interface RedisZSetRequest {

	Long getStart();

	Integer getSize();

	default Long getEnd() {
		return getStart() + getSize();
	}
}
