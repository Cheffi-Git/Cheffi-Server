package com.cheffi.review.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.avatar.service.AvatarService;
import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.BusinessException;
import com.cheffi.review.domain.Bookmark;
import com.cheffi.review.repository.BookmarkRepository;
import com.cheffi.util.model.ExactPeriod;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BookmarkService {
	private final AvatarService avatarService;
	private final ReviewService reviewService;
	private final BookmarkRepository bookmarkRepository;

	@Transactional
	public Long addBookmark(Long avatarId, Long reviewId) {
		if (hasBookmarked(avatarId, reviewId))
			throw new BusinessException(ErrorCode.ALREADY_BOOKMARKED);
		Bookmark bookmark = bookmarkRepository.save(
			Bookmark.of(avatarService.getById(avatarId), reviewService.getById(reviewId)));
		return bookmark.getId();
	}

	@Transactional
	public void cancelBookmark(Long avatarId, Long reviewId) {
		bookmarkRepository.delete(getByAvatarAndReview(avatarId, reviewId));
	}

	public boolean hasBookmarked(Long avatarId, Long reviewId) {
		return bookmarkRepository.existsByAvatarAndReview(avatarId, reviewId);
	}

	private Bookmark getByAvatarAndReview(Long avatarId, Long reviewId) {
		return bookmarkRepository.findByAvatarAndReview(avatarId, reviewId)
			.orElseThrow(() -> new BusinessException(ErrorCode.NOT_BOOKMARKED));
	}

	public List<Bookmark> getByReviewLockBetween(ExactPeriod ep) {
		return bookmarkRepository.findByReviewLockBetween(ep.getStart(), ep.getEnd());
	}
}
