package com.cheffi.tag.constant;

import static com.cheffi.tag.constant.TagType.*;

public enum TagLimit {
	REVIEW(1, 2),
	AVATAR(3, 5);

	private final int foodMin;
	private final int tasteMin;

	TagLimit(int foodMin, int tasteMin) {
		this.foodMin = foodMin;
		this.tasteMin = tasteMin;
	}

	public int getMin(TagType type) {
		if (type.equals(FOOD))
			return this.foodMin;
		return this.tasteMin;
	}

}
