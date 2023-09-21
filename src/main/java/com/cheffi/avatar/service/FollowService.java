package com.cheffi.avatar.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.avatar.domain.Avatar;
import com.cheffi.avatar.domain.Follow;
import com.cheffi.avatar.dto.response.AddFollowResponse;
import com.cheffi.avatar.dto.response.GetFollowResponse;
import com.cheffi.avatar.dto.response.RecommendFollowResponse;
import com.cheffi.avatar.dto.response.UnfollowResponse;
import com.cheffi.avatar.repository.FollowRepository;
import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.BusinessException;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class FollowService {

	private final FollowRepository followRepository;
	private final AvatarService avatarService;


	@Transactional
	public AddFollowResponse addFollow(Long followerId, Long followeeId) {

		Avatar follower = avatarService.getById(followerId);
		Avatar followee = avatarService.getById(followeeId);

		if (followRepository.existsBySubjectAndTarget(followee, follower)) {
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


	public List<GetFollowResponse> getFollowee(Long userId) {
		return GetFollowResponse.mock();
	}

	public List<RecommendFollowResponse> recommendFollowee(Long userId) {
		return RecommendFollowResponse.mock();
	}


	public Follow getByFollowerAndFollowee(Avatar follower, Avatar followee) {
		return followRepository.findBySubjectAndTarget(follower, followee)
			.orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOLLOWED));
	}
}