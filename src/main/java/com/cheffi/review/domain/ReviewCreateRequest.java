package com.cheffi.review.domain;

public record ReviewCreateRequest(
	String title,
	String text,
	int lockAfterHours
) {
}
