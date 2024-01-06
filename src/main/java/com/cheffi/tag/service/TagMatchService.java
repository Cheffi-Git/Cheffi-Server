package com.cheffi.tag.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.tag.domain.Tag;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class TagMatchService {

	private final TagService tagService;

	public Long getMatchedNumberOfTags(Long avatarId, Long reviewId) {
		List<Tag> tagsOfAvatar = tagService.getAvatarTagByAvatarId(avatarId);
		List<Tag> tagsOfReview = tagService.getReviewTagByReviewId(reviewId);
		return tagsOfAvatar.stream().filter(tagsOfReview::contains).count();
	}
}
