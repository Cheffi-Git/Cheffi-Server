package com.cheffi.tag.service.validation;

import java.util.List;

import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.BusinessException;
import com.cheffi.tag.constant.TagLimit;
import com.cheffi.tag.constant.TagType;
import com.cheffi.tag.domain.Tag;

public interface TagValidationPolicy {

	void verifyTagsByType(List<Tag> tags, TagType type);

	default void validateTagsByType(List<Tag> tags, TagType type, TagLimit limit) {
		if(tags.size() < limit.getMin(type))
			throw new BusinessException(ErrorCode.LOWER_LIMIT_UNSATISFIED);

		verifyTagsByType(tags, type);
	}

}
