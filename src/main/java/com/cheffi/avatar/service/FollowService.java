package com.cheffi.avatar.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.avatar.domain.Avatar;
import com.cheffi.avatar.domain.Follow;
import com.cheffi.avatar.dto.GetFollowRequest;
import com.cheffi.avatar.dto.response.AddFollowResponse;
import com.cheffi.avatar.dto.response.GetFollowResponse;
import com.cheffi.avatar.dto.response.RecommendFollowResponse;
import com.cheffi.avatar.dto.response.UnfollowResponse;
import com.cheffi.avatar.repository.FollowJpaRepository;
import com.cheffi.avatar.repository.FollowRepository;
import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.BusinessException;
import com.cheffi.common.dto.CursorPage;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class FollowService {

	private final FollowRepository followRepository;
	private final FollowJpaRepository followJpaRepository;
	private final AvatarService avatarService;

	@Transactional
	public AddFollowResponse addFollow(Long followerId, Long followeeId) {

		Avatar follower = avatarService.getById(followerId);
		Avatar followee = avatarService.getById(followeeId);

		if (followRepository.existsBySubjectAndTarget(followerId, followeeId)) {
			throw new BusinessException(ErrorCode.ALREADY_FOLLOWED);
		}

		Follow createdFollow = followRepository.save(Follow.createFollowRelationship(follower, followee));
		follower.addFollowing();
		followee.addFollower();

		return AddFollowResponse.from(createdFollow);
	}

	@Transactional
	public UnfollowResponse unfollow(Long followerId, Long followeeId) {

		Avatar follower = avatarService.getById(followerId);
		Avatar followee = avatarService.getById(followeeId);

		followRepository.delete(getByFollowerAndFollowee(follower, followee));
		follower.removeFollowing();
		followee.removeFollower();

		return new UnfollowResponse(followerId, followeeId);
	}

	@Transactional
	public void destroyFriendship(Long firstId, Long secondId) {
		if (followRepository.existsBySubjectAndTarget(firstId, secondId))
			unfollow(firstId, secondId);
		if (followRepository.existsBySubjectAndTarget(secondId, firstId))
			unfollow(secondId, firstId);
	}

	public CursorPage<GetFollowResponse, Long> getFollowing(GetFollowRequest request, Long followerId, Long viewerId) {
		return CursorPage.of(followJpaRepository.findFollowing(request, followerId, viewerId),
			request.getSize(), GetFollowResponse::cursor);
	}

	public CursorPage<GetFollowResponse, Long> getFollower(GetFollowRequest request, Long followingId, Long viewerId) {
		return CursorPage.of(followJpaRepository.findFollower(request, followingId, viewerId),
			request.getSize(), GetFollowResponse::cursor);
	}

	public List<RecommendFollowResponse> recommendFollowee(Long tagId, Long avatarId) {
		return followJpaRepository.recommendByTag(tagId, avatarId);
	}

	public Follow getByFollowerAndFollowee(Avatar follower, Avatar followee) {
		return followRepository.findBySubjectAndTarget(follower, followee)
			.orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOLLOWED));
	}
}
