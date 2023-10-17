package com.cheffi.review.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.avatar.dto.common.TagDto;
import com.cheffi.avatar.dto.request.TagsChangeRequest;
import com.cheffi.avatar.dto.response.TagsChangeResponse;
import com.cheffi.review.domain.Review;
import com.cheffi.review.domain.ReviewTag;
import com.cheffi.tag.constant.TagType;
import com.cheffi.tag.domain.Tag;
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
	 * @param tag    변경하고자하는 Tag 의 식별자를 담은 Dto 입니다. 반드시 변경을 원하는 최종 상태의
	 *               식별자들을 갖고 있어야 합니다.
	 */
	@Transactional
	public TagsChangeResponse changeTags(Review review, TagsChangeRequest tag) {
		List<Tag> tagsFromReview = getTagsFromReview(review);

		List<Tag> tagsFromDB = tagService.getAllById(tag.combinedList());
		tagValidationPolicy.validateTagsOfReview(tagsFromDB, tag.foodTags(), tag.tasteTags());

		List<Tag> tagsToAdd = tagService.extractDistinctTags(tagsFromDB, tagsFromReview);
		List<Tag> tagsToRemove = tagService.extractDistinctTags(tagsFromReview, tagsFromDB);

		review.removeTags(tagsToRemove);
		review.addTags(tagsToAdd);

		return new TagsChangeResponse(getTagDtoByType(review, TagType.FOOD), getTagDtoByType(review, TagType.TASTE));
	}

	private List<Tag> getTagsFromReview(Review review) {
		return review.getReviewTags().stream().map(ReviewTag::getTag).toList();
	}

	private List<TagDto> getTagDtoByType(Review review, TagType type) {
		return review.getReviewTags().stream()
			.map(ReviewTag::getTag)
			.filter(t -> t.getTagType().equals(type))
			.map(TagDto::of)
			.toList();
	}
}
