package com.cheffi.avatar.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.avatar.domain.Avatar;
import com.cheffi.avatar.domain.Follow;
import com.cheffi.avatar.dto.response.AddFollowResponse;
import com.cheffi.avatar.dto.response.GetMyFolloweeData;
import com.cheffi.avatar.dto.response.RecommendFollowResponse;
import com.cheffi.avatar.dto.response.UnfollowResponse;
import com.cheffi.avatar.repository.FollowJpaRepository;
import com.cheffi.avatar.repository.FollowRepository;
import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.BusinessException;
import com.cheffi.common.config.exception.business.EntityNotFoundException;
import com.cheffi.common.request.CursorPageable;
import com.cheffi.common.response.CursorPageResponse;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class FollowService {

	private final FollowJpaRepository followJpaRepository;
	private final FollowRepository followRepository;
	private final AvatarService avatarService;
	private static final Long INVALID_FOLLOW_ID = -1L;


	@Transactional
	public AddFollowResponse addFollow(Long followerId, Long followeeId) {

		Avatar follower = avatarService.getById(followerId);
		Avatar followee = avatarService.getById(followeeId);

		if (followRepository.existsBySubjectAndTarget(follower, followee)) {
			throw new BusinessException(ErrorCode.ALREADY_FOLLOWED);
		}

		Follow createdFollow = followRepository.save(Follow.createFollowRelationship(follower, followee));

		return AddFollowResponse.of(createdFollow);
	}

	@Transactional
	public UnfollowResponse unfollow(Long followerId, Long followeeId) {

		Avatar follower = avatarService.getById(followerId);
		Avatar followee = avatarService.getById(followeeId);

		followRepository.delete(getByFollowerAndFollowee(follower, followee));

		return new UnfollowResponse(followerId, followeeId);
	}

	public CursorPageResponse<List<GetMyFolloweeData>> getFollowee(Long avatarId, CursorPageable pageable) {

		if (!avatarService.existsById(avatarId)) {
			throw new EntityNotFoundException(ErrorCode.AVATAR_NOT_EXISTS);
		}

		List<Follow> follows = followJpaRepository.getFollows(avatarId, pageable);
		boolean hasLastPageCursor = pageable.size() == follows.size();
		Long lastFollowIdForCursor = INVALID_FOLLOW_ID;

		int lastIndex = follows.isEmpty() ? -1 : follows.size() - 1;
		if (hasLastPageCursor) {
			lastFollowIdForCursor = follows.get(lastIndex - 1).getId();
			follows.remove(lastIndex);
		}

		else if (!follows.isEmpty()) {
			lastFollowIdForCursor = follows.get(lastIndex).getId();
		}

		return CursorPageResponse.success(
			GetMyFolloweeData.mapFollowsToData(follows),
			lastFollowIdForCursor.equals(INVALID_FOLLOW_ID)
				? ""
				: pageable.getEncodedCursor(lastFollowIdForCursor, hasLastPageCursor)

		);

	}

	public List<RecommendFollowResponse> recommendFollowee(Long userId) {
		return RecommendFollowResponse.mock();
	}

	public Follow getByFollowerAndFollowee(Avatar follower, Avatar followee) {
		return followRepository.findBySubjectAndTarget(follower, followee)
			.orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOLLOWED));
	}
}