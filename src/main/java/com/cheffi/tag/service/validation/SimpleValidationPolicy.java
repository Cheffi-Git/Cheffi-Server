package com.cheffi.tag.service.validation;

import java.util.List;

import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.BusinessException;
import com.cheffi.tag.constant.TagType;
import com.cheffi.tag.domain.Tag;

public class SimpleValidationPolicy implements TagValidationPolicy {

	@Override
	public void verifyTagsByType(List<Tag> tags, TagType type) {
		for (Tag tag : tags) {
			if(!tag.hasType(type))
				throw new BusinessException(ErrorCode.TAG_UNMATCHED);
		}
	}

}
