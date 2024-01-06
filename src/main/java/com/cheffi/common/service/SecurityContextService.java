package com.cheffi.common.service;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.cheffi.common.auth.Authorities;
import com.cheffi.oauth.model.UserPrincipal;
import com.cheffi.user.constant.RoleType;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SecurityContextService {

	private final SecurityContextHolderStrategy securityContextHolderStrategy =
		SecurityContextHolder.getContextHolderStrategy();

	private final SecurityContextRepository securityContextRepository =
		new HttpSessionSecurityContextRepository();

	public void saveToSecurityContext(AbstractAuthenticationToken authenticationToken) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authenticationToken);
		securityContextHolderStrategy.setContext(context);
		securityContextRepository.saveContext(context, getRequest(), getResponse());
	}

	public void updatePrincipal(UserPrincipal principal) {
		SecurityContext context = getContext();
		Authentication authentication = context.getAuthentication();

		PreAuthenticatedAuthenticationToken token =
			new PreAuthenticatedAuthenticationToken(principal, authentication.getCredentials(),
				principal.getAuthorities());
		context.setAuthentication(token);

		securityContextRepository.saveContext(context, getRequest(), getResponse());
	}

	private static SecurityContext getContext() {
		return SecurityContextHolder.getContext();
	}

	private static HttpServletRequest getRequest() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes instanceof ServletRequestAttributes attributes) {
			return attributes.getRequest();
		}
		return null;
	}

	private static HttpServletResponse getResponse() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes instanceof ServletRequestAttributes attributes) {
			return attributes.getResponse();
		}
		return null;
	}

	public UserPrincipal getUserPrincipal() {
		return (UserPrincipal)getContext().getAuthentication().getPrincipal();
	}

	public boolean hasUserAuthority(UserPrincipal principal) {
		return principal != null && principal.getAuthorities()
			.contains(new SimpleGrantedAuthority(RoleType.USER.getAuthority()));
	}

	public Authorities getAuthorities() {
		return new Authorities(getUserPrincipal().getAuthorities());
	}
}
