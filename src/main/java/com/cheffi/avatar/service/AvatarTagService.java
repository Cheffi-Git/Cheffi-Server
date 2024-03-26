package com.cheffi.avatar.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.avatar.domain.Avatar;
import com.cheffi.avatar.domain.AvatarTag;
import com.cheffi.avatar.dto.common.TagDto;
import com.cheffi.avatar.dto.response.TagsChangeResponse;
import com.cheffi.avatar.repository.AvatarRepository;
import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.BusinessException;
import com.cheffi.common.config.exception.business.EntityNotFoundException;
import com.cheffi.tag.constant.TagLimit;
import com.cheffi.tag.constant.TagType;
import com.cheffi.tag.domain.Tag;
import com.cheffi.tag.dto.request.TagsChangeRequest;
import com.cheffi.tag.service.TagService;
import com.cheffi.tag.service.validation.TagValidationPolicy;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AvatarTagService {

	private final AvatarRepository avatarRepository;
	private final TagService tagService;
	private final TagValidationPolicy tagValidationPolicy;

	@Transactional
	public TagsChangeResponse changeTagsByType(Long avatarId, TagsChangeRequest request) {
		if (TagType.ALL.equals(request.type()))
			throw new BusinessException(ErrorCode.ALL_TYPE_CANNOT_BE_INCLUDED);

		Avatar avatar = getByIdWithTags(avatarId);
		List<Tag> originalTags = avatar.getTags(request.type());

		List<Tag> requestedTags = tagService.getAllById(request.ids());
		tagValidationPolicy.validateTagsByType(requestedTags, request.type(), TagLimit.AVATAR);

		List<Tag> tagsToAdd = tagService.extractDistinctTags(requestedTags, originalTags);
		List<Tag> tagsToRemove = tagService.extractDistinctTags(originalTags, requestedTags);

		avatar.addTags(tagsToAdd);
		avatar.removeTags(tagsToRemove);

		return new TagsChangeResponse(getTagDto(avatar), request.type());
	}

	private List<TagDto> getTagDto(Avatar avatar) {
		return avatar.getAvatarTags().stream()
			.map(AvatarTag::getTag)
			.map(TagDto::of)
			.toList();
	}

	public Avatar getByIdWithTags(Long avatarId) {
		return avatarRepository.findByIdWithTags(avatarId)
			.orElseThrow(() -> new EntityNotFoundException(ErrorCode.AVATAR_NOT_EXISTS));
	}

}
