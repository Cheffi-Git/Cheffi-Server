package com.cheffi.oauth.model;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.cheffi.avatar.domain.Avatar;
import com.cheffi.user.domain.User;

public class AuthenticationToken extends AbstractAuthenticationToken {

	private final UserPrincipal principal;
	private String idToken;

	public AuthenticationToken(
		UserPrincipal principal,
		String idToken,
		Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		super.setAuthenticated(true);
		this.principal = principal;
		this.idToken = idToken;
	}

	@Override
	public Object getCredentials() {
		return idToken;
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}

	public static AuthenticationToken of(User user, Avatar avatar, String idToken,
		Set<SimpleGrantedAuthority> authorities) {
		return new AuthenticationToken(UserPrincipal.of(user, avatar, authorities), idToken, authorities);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof AuthenticationToken that))
			return false;
		if (!super.equals(o))
			return false;
		return Objects.equals(idToken, that.idToken);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), idToken);
	}

	/**
	 * TODO 테스트용 토큰 발급 메서드로 프로덕션에서는 반드시 비활성화 필요
	 */
	public static AuthenticationToken mock(List<SimpleGrantedAuthority> authorities) {
		return new AuthenticationToken(UserPrincipal.mock(authorities), "test_id_token", authorities);
	}

}
