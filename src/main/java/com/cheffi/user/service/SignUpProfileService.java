package com.cheffi.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.avatar.service.AvatarService;
import com.cheffi.common.aspect.annotation.UpdatePrincipal;
import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.BusinessException;
import com.cheffi.user.domain.User;
import com.cheffi.user.dto.adapter.UserInfo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SignUpProfileService {

	private final UserService userService;
	private final AvatarService avatarService;

	@UpdatePrincipal
	@Transactional
	public UserInfo completeProfile(Long userId, Long avatarId) {
		if (!userService.hasNoProfileRole(userId))
			throw new BusinessException(ErrorCode.PROFILE_ALREADY_REGISTERED);
		if (!avatarService.checkIfCompleteProfile(avatarId))
			throw new BusinessException(ErrorCode.PROFILE_NOT_COMPLETED);

		User user = userService.removeNoProfileRole(userId);
		return UserInfo.of(user, user.getRoles());
	}
}
