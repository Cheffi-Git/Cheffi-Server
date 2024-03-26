package com.cheffi.review.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.review.dto.RatingInfoDto;
import com.cheffi.review.dto.ReviewViewerInfoDto;
import com.cheffi.tag.service.TagMatchService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class ReviewAvatarService {

	private final BookmarkService bookmarkService;
	private final TagMatchService tagMatchService;
	private final RatingService ratingService;

	public ReviewViewerInfoDto getInfoOfViewer(Long avatarId, Long reviewId) {
		// 북마크 조회
		boolean bookmarked = bookmarkService.hasBookmarked(avatarId, reviewId);

		// 일치하는 태그의 개수
		Long matchedTagNum = tagMatchService.getMatchedNumberOfTags(avatarId, reviewId);

		// 유저의 평가정보
		RatingInfoDto ratingInfoDto = ratingService.getRatingInfoOf(avatarId, reviewId);
		return ReviewViewerInfoDto.builder()
			.bookmarked(bookmarked)
			.matchedTagNum(matchedTagNum)
			.ratedByUser(ratingInfoDto.isRated())
			.ratingType(ratingInfoDto.getRatingType())
			.build();
	}
}
