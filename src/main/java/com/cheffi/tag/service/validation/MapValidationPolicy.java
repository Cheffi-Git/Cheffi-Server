package com.cheffi.tag.service.validation;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.BusinessException;
import com.cheffi.tag.constant.TagType;
import com.cheffi.tag.domain.Tag;

public class MapValidationPolicy extends TagValidationPolicy {
	public void validateTagsOfAvatar(List<Tag> tags, List<Long> foodTagIds, List<Long> tasteTagIds) {
		checkAvatarSize(tags, foodTagIds, tasteTagIds);
		Map<Long, Tag> tagMap = tags.stream()
			.collect(Collectors.toMap(Tag::getId, t -> t));

		verifyPresence(tagMap, foodTagIds, TagType.FOOD);
		verifyPresence(tagMap, tasteTagIds, TagType.TASTE);
	}

	@Override
	public void validateTagsOfReview(List<Tag> tags, List<Long> foodTagIds, List<Long> tasteTagIds) {
		checkReviewSize(tags, foodTagIds, tasteTagIds);
		Map<Long, Tag> tagMap = tags.stream()
			.collect(Collectors.toMap(Tag::getId, t -> t));

		verifyPresence(tagMap, foodTagIds, TagType.FOOD);
		verifyPresence(tagMap, tasteTagIds, TagType.TASTE);
	}

	private void verifyPresence(Map<Long, Tag> tagMap, List<Long> foodTagIds, TagType requiredType) {
		for (Long id : foodTagIds) {
			Tag tag = tagMap.get(id);
			if (tag == null)
				throw new BusinessException(ErrorCode.BAD_AVATAR_TAG_REQUEST);
			if (!tag.getTagType().equals(requiredType))
				throw new BusinessException(ErrorCode.BAD_AVATAR_TAG_REQUEST);
		}
	}
}
