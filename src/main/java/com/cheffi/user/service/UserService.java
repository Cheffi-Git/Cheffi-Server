package com.cheffi.user.service;

import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.avatar.domain.Avatar;
import com.cheffi.avatar.repository.AvatarRepository;
import com.cheffi.common.aspect.annotation.UpdatePrincipal;
import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.BusinessException;
import com.cheffi.user.constant.RoleType;
import com.cheffi.user.constant.UserType;
import com.cheffi.user.domain.User;
import com.cheffi.user.dto.UserCreateRequest;
import com.cheffi.user.dto.adapter.UserInfo;
import com.cheffi.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

	private final UserRepository userRepository;
	private final AvatarRepository avatarRepository;
	private final RoleService roleService;

	@UpdatePrincipal
	public UserInfo getUserInfo(Long userId) {
		User user = getByIdWithRoles(userId);
		return UserInfo.of(user, user.getRoles());
	}

	public User getByIdWithRoles(Long userId) {
		return userRepository.findByIdWithRoles(userId)
			.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_EXISTS));
	}

	@Transactional
	public User signUp(UserCreateRequest request) {
		if (request.userType().equals(UserType.LOCAL))
			throw new BusinessException(ErrorCode.EMAIL_LOGIN_NOT_SUPPORTED);
		User user = userRepository.save(User.createUser(request));
		avatarRepository.save(new Avatar(getRandomString() + "쉐피", user));
		return user;
	}

	private String getRandomString() {
		return RandomStringUtils.randomNumeric(6);
	}

	public Optional<User> getByEmailWithRoles(String email) {
		return userRepository.findByEmailWithRoles(email);
	}

	@UpdatePrincipal
	@Transactional
	public UserInfo changeTermsAgreement(Long userId, Boolean adAgreed, Boolean analysisAgreed) {
		User user = getById(userId);
		user.changeTermsAgreement(adAgreed, analysisAgreed);
		return UserInfo.of(user);
	}

	public User getById(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_EXISTS));
	}

	public boolean hasNoProfileRole(Long userId) {
		User user = getByIdWithRoles(userId);
		return user.getUserRoles().stream()
			.anyMatch(ur -> ur.getRole().getRoleType().equals(RoleType.NO_PROFILE));
	}

	@Transactional
	public User removeNoProfileRole(Long userId) {
		User user = getByIdWithRoles(userId);
		user.removeRole(roleService.getNoProfileRole());
		return user;
	}
}
