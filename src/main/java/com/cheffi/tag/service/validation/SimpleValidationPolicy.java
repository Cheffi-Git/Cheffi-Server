package com.cheffi.tag.service.validation;

import java.util.List;

import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.BusinessException;
import com.cheffi.tag.constant.TagType;
import com.cheffi.tag.domain.Tag;

public class SimpleValidationPolicy extends TagValidationPolicy {
	@Override
	public void validateTagsOfAvatar(List<Tag> tags, List<Long> foodTagIds, List<Long> tasteTagIds) {
		checkAvatarSize(tags, foodTagIds, tasteTagIds);
		verifyPresence(tags, foodTagIds, TagType.FOOD);
		verifyPresence(tags, tasteTagIds, TagType.TASTE);
	}

	@Override
	public void validateTagsOfReview(List<Tag> tags, List<Long> foodTagIds, List<Long> tasteTagIds) {
		checkReviewSize(tags, foodTagIds, tasteTagIds);
		verifyPresence(tags, foodTagIds, TagType.FOOD);
		verifyPresence(tags, tasteTagIds, TagType.TASTE);
	}

	private void verifyPresence(List<Tag> tags, List<Long> requestedTagIds, TagType requiredType) {
		for (Long id : requestedTagIds) {
			boolean absent = true;
			for (Tag tag : tags) {
				if (tag.getId().equals(id)) {
					if (!tag.getTagType().equals(requiredType))
						throw new BusinessException(ErrorCode.BAD_AVATAR_TAG_REQUEST);
					absent = false;
					break;
				}
			}
			if (absent)
				throw new BusinessException(ErrorCode.BAD_AVATAR_TAG_REQUEST);
		}
	}
}
