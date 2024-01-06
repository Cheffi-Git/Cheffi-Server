package com.cheffi.profile.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.avatar.service.AvatarService;
import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.BusinessException;
import com.cheffi.cs.service.BlockService;
import com.cheffi.profile.dto.MyPageInfo;
import com.cheffi.profile.dto.ProfileInfo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProfileService {

	private final AvatarService avatarService;
	private final BlockService blockService;

	public MyPageInfo getMyPageInfo(Long avatarId) {
		return avatarService.getMyPageInfo(avatarId);
	}

	public ProfileInfo getProfile(Long ownerId, Long viewerId) {
		// owner 가 조회자를 차단하면 조회 불가
		if (viewerId != null && blockService.checkBlocked(ownerId, viewerId))
			throw new BusinessException(ErrorCode.VIEWER_IS_BLOCKED);
		return avatarService.getProfile(ownerId, viewerId);
	}

}
