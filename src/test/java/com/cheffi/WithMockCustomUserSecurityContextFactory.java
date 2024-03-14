package com.cheffi;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.cheffi.oauth.model.AuthenticationToken;
import com.cheffi.oauth.model.UserPrincipal;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

	@Override
	public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
		final SecurityContext context = SecurityContextHolder.getContext();

		List<SimpleGrantedAuthority> roleUser = List.of(new SimpleGrantedAuthority(annotation.role()));
		AuthenticationToken authentication = new AuthenticationToken(UserPrincipal.mock(roleUser), "IDTOKEN",
			roleUser);

		context.setAuthentication(authentication);
		return context;
	}

}
