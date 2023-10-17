package com.cheffi.tag.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.avatar.dto.common.TagDto;
import com.cheffi.tag.constant.TagType;
import com.cheffi.tag.domain.Tag;
import com.cheffi.tag.repository.TagRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TagService {
	private final TagRepository tagRepository;

	public List<TagDto> getTagsByType(TagType type) {
		return tagRepository.findByTagType(type).stream().map(TagDto::of)
			.toList();
	}

	public List<Tag> getAllById(List<Long> ids) {
		return tagRepository.findAllById(ids);
	}

	public List<Tag> extractDistinctTags(List<Tag> src, List<Tag> from) {
		return src.stream()
			.filter(t -> !from.contains(t))
			.toList();
	}

	public List<Tag> getAvatarTagByAvatarId(Long avatarId) {
		return tagRepository.findByAvatarId(avatarId);
	}

	public List<Tag> getReviewTagByReviewId(Long reviewId) {
		return tagRepository.findByReviewId(reviewId);
	}

}
