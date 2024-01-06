package com.cheffi.tag.service.validation;

import java.util.List;

import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.BusinessException;
import com.cheffi.tag.domain.Tag;

public abstract class TagValidationPolicy {
	private static final int AVATAR_TASTE_TAG_MIN = 5;
	private static final int AVATAR_FOOD_TAG_MIN = 3;
	private static final int REVIEW_FOOD_TAG_MIN = 1;
	private static final int REVIEW_TASTE_TAG_MIN = 2;

	public abstract void validateTagsOfAvatar(List<Tag> tags, List<Long> foodTagIds, List<Long> tasteTagIds);

	public abstract void validateTagsOfReview(List<Tag> tags, List<Long> foodTagIds, List<Long> tasteTagIds);

	private void checkSize(List<Tag> tags, List<Long> foodTagIds, List<Long> tasteTagIds) {
		if (tags.size() != foodTagIds.size() + tasteTagIds.size())
			throw new BusinessException(ErrorCode.BAD_AVATAR_TAG_REQUEST);
	}

	protected void checkAvatarSize(List<Tag> tags, List<Long> foodTagIds, List<Long> tasteTagIds) {
		checkSize(tags, foodTagIds, tasteTagIds);

		if (foodTagIds.size() < AVATAR_FOOD_TAG_MIN || tasteTagIds.size() < AVATAR_TASTE_TAG_MIN)
			throw new BusinessException(ErrorCode.BAD_AVATAR_TAG_REQUEST);
	}

	protected void checkReviewSize(List<Tag> tags, List<Long> foodTagIds, List<Long> tasteTagIds) {
		checkSize(tags, foodTagIds, tasteTagIds);

		if (foodTagIds.size() < REVIEW_FOOD_TAG_MIN || tasteTagIds.size() < REVIEW_TASTE_TAG_MIN)
			throw new BusinessException(ErrorCode.BAD_AVATAR_TAG_REQUEST);
	}

}
