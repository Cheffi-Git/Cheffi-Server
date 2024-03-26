package com.cheffi.oauth.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cheffi.avatar.domain.Avatar;
import com.cheffi.avatar.dto.adapter.SelfAvatarInfo;
import com.cheffi.user.constant.UserType;
import com.cheffi.user.domain.User;
import com.cheffi.user.dto.adapter.UserInfo;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class UserPrincipal implements UserDetails, Serializable {

	// User
	private final Long userId;
	private final String email;
	private boolean locked;
	private boolean expired;
	private boolean activated;
	private LocalDateTime lastPwChangedDate;
	private String name;
	private final UserType userType;
	private boolean adAgreed;
	private boolean analysisAgreed;
	private String fcmToken;

	// Avatar
	private final Long avatarId;
	private String nickname;

	// Role
	private List<SimpleGrantedAuthority> authorities;

	public static UserPrincipal of(User user, Avatar avatar, Set<SimpleGrantedAuthority> authorities) {
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

	public UserPrincipal update(UserInfo info) {
		this.lastPwChangedDate = info.lastPwChangedDate();
		this.name = info.name();
		this.adAgreed = info.adAgreed();
		this.analysisAgreed = info.analysisAgreed();
		this.fcmToken = info.fcmToken();
		if (info.authorities() != null)
			this.authorities = info.authorities().stream().map((SimpleGrantedAuthority::new)).toList();
		return this;
	}

	public UserPrincipal update(SelfAvatarInfo info) {
		this.nickname = info.nickname();
		return this;
	}

	@Override
	public List<SimpleGrantedAuthority> getAuthorities() {
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
	public static UserPrincipal mock(List<SimpleGrantedAuthority> authorities) {
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
			.authorities(authorities)
			.build();
	}

}
