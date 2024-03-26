package com.cheffi.oauth.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.avatar.domain.Avatar;
import com.cheffi.avatar.service.AvatarService;
import com.cheffi.avatar.service.CheffiCoinService;
import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.AuthenticationException;
import com.cheffi.common.config.exception.business.DuplicatedEmailException;
import com.cheffi.common.service.SecurityContextService;
import com.cheffi.oauth.dto.IdTokenAttributes;
import com.cheffi.oauth.dto.request.OidcLoginRequest;
import com.cheffi.oauth.dto.response.OidcLoginResponse;
import com.cheffi.oauth.model.AuthenticationToken;
import com.cheffi.oauth.model.OAuthAttributes;
import com.cheffi.user.domain.User;
import com.cheffi.user.service.RoleService;
import com.cheffi.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OAuthService {
	private final UserService userService;
	private final AvatarService avatarService;
	private final RoleService roleService;
	private final SecurityContextService securityContextService;
	private final Map<String, OidcLoginApiService> providerMap;
	private final CheffiCoinService cheffiCoinService;

	@Transactional
	public OidcLoginResponse oidcLogin(OidcLoginRequest loginRequest, String provider) {
		OidcLoginApiService apiService = providerMap.get(provider.toLowerCase());
		if (apiService == null)
			throw new AuthenticationException(ErrorCode.NOT_SUPPORTED_OAUTH_PROVIDER);

		// ID 토큰 검증
		IdTokenAttributes attributes = apiService.verify(loginRequest.token(), loginRequest.platform().toString());

		// 사용자 정보 가져오기
		OAuthAttributes oAuthAttributes = apiService.getUserInfo(attributes);

		Optional<User> optionalUser = userService.getByEmailWithRoles(oAuthAttributes.email());
		boolean isNewUser = optionalUser.isEmpty();
		User user = optionalUser.orElseGet(() -> signUp(oAuthAttributes));

		// 이메일로 등록된 유저의 플랫폼과 현재 로그인을 시도하는 플랫폼과 다르면 에러 throw
		if (!isNewUser && !user.isUserOf(provider))
			throw new DuplicatedEmailException(ErrorCode.EMAIL_IS_REGISTER_WITH_ANOTHER_PROVIDER, user.getUserType());

		Avatar avatar = avatarService.getByUserWithPhoto(user);
		if (isNewUser || !user.hasLoggedInToday()) {
			user.updateLastLoginDate();
			cheffiCoinService.earnCheffiCoinForLogin(avatar.getId());
		}

		Set<SimpleGrantedAuthority> authorities = getAuthoritiesFromUser(user);
		AuthenticationToken authenticationToken =
			AuthenticationToken.of(user, avatar, loginRequest.token(), authorities);

		securityContextService.saveToSecurityContext(authenticationToken);
		return OidcLoginResponse.of(authenticationToken, avatar, avatar.getPhoto(), isNewUser);
	}

	private User signUp(OAuthAttributes oAuthAttributes) {
		return userService.signUp(oAuthAttributes
			.toUserCreateRequest(List.of(roleService.getUserRole(), roleService.getNoProfileRole())));
	}

	private Set<SimpleGrantedAuthority> getAuthoritiesFromUser(User user) {
		return user.getUserRoles()
			.stream()
			.map(ur ->
				new SimpleGrantedAuthority(ur.getRole().getAuthority()))
			.collect(Collectors.toSet());
	}

}
