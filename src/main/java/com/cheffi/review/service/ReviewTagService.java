package com.cheffi.review.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.BusinessException;
import com.cheffi.review.domain.Review;
import com.cheffi.tag.constant.TagLimit;
import com.cheffi.tag.constant.TagType;
import com.cheffi.tag.domain.Tag;
import com.cheffi.tag.dto.request.TagsChangeRequest;
import com.cheffi.tag.service.TagService;
import com.cheffi.tag.service.validation.TagValidationPolicy;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ReviewTagService {

	private final TagService tagService;
	private final TagValidationPolicy tagValidationPolicy;

	/**
	 * @param review 전달되는 review Entity 는 DB에 매핑된 레코드가 없는 상태이거나
	 *               fetch join 을 통해서 ReviewTag 를 영속성 컨텍스트내에 갖고 있어야 합니다.
	 */

	@Transactional
	public void changeTags(Review review, Map<TagType, List<Long>> map) {
		for (TagType type : TagType.values()) {
			if (!type.equals(TagType.ALL) && !map.containsKey(type))
				throw new BusinessException(ErrorCode.SOME_TAGS_ARE_MISSING);
		}

		for (var entry : map.entrySet()) {
			changeTagsByType(review, new TagsChangeRequest(entry.getValue(), entry.getKey()));
		}
	}

	@Transactional
	public void changeTagsByType(Review review, TagsChangeRequest request) {
		if (TagType.ALL.equals(request.type()))
			throw new BusinessException(ErrorCode.ALL_TYPE_CANNOT_BE_INCLUDED);

		List<Tag> originalTags = review.getTags(request.type());

		List<Tag> requestedTags = tagService.getAllById(request.ids());
		tagValidationPolicy.validateTagsByType(requestedTags, request.type(), TagLimit.REVIEW);

		List<Tag> tagsToAdd = tagService.extractDistinctTags(requestedTags, originalTags);
		List<Tag> tagsToRemove = tagService.extractDistinctTags(originalTags, requestedTags);

		review.removeTags(tagsToRemove);
		review.addTags(tagsToAdd);
	}

}
