package com.cheffi.oauth.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cheffi.avatar.domain.Avatar;
import com.cheffi.user.constant.UserType;
import com.cheffi.user.domain.User;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class UserPrincipal implements UserDetails, Serializable {

	private final Long userId;
	private final String email;
	private final boolean locked;
	private final boolean expired;
	private final boolean activated;
	private final LocalDateTime lastPwChangedDate;
	private final String name;
	private final UserType userType;
	private final boolean adAgreed;
	private final boolean analysisAgreed;
	private final String fcmToken;
	private final Long avatarId;
	private final String nickname;
	private final List<GrantedAuthority> authorities;

	public static UserPrincipal of(User user, Avatar avatar, Collection<? extends GrantedAuthority> authorities) {
		return UserPrincipal.builder()
			.userId(user.getId())
			.email(user.getEmail())
			.locked(user.isLocked())
			.expired(user.isExpired())
			.activated(user.isActivated())
			.lastPwChangedDate(user.getLastPwChangedDate())
			.name(user.getName())
			.userType(user.getUserType())
			.adAgreed(user.isAdAgreed())
			.analysisAgreed(user.isAnalysisAgreed())
			.fcmToken(user.getFcmToken())
			.avatarId(avatar.getId())
			.nickname(avatar.getNickname())
			.authorities(new ArrayList<>(authorities))
			.build();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return name;
	}

	@Override
	public boolean isAccountNonExpired() {
		return !expired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !locked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return activated;
	}

	/**
	 * TODO 테스트용 토큰 발급을 위한 메서드로 프로덕션에서는 반드시 비활성화 필요
	 */
	public static UserPrincipal mock(Collection<? extends GrantedAuthority> authorities) {
		return UserPrincipal.builder()
			.userId(34L)
			.email("Mock@mock.com")
			.expired(false)
			.locked(false)
			.name("안유진")
			.userType(UserType.KAKAO)
			.activated(true)
			.lastPwChangedDate(LocalDateTime.now().minusWeeks(2))
			.userType(UserType.KAKAO)
			.adAgreed(true)
			.analysisAgreed(false)
			.fcmToken("fcm-token")
			.avatarId(34L)
			.nickname("댕댕이")
			.authorities(new ArrayList<>(authorities))
			.build();
	}

}
