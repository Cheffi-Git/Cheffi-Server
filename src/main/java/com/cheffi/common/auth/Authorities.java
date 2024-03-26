package com.cheffi.common.auth;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class Authorities {

	private final List<SimpleGrantedAuthority> simpleAuthorities;

	public Authorities(List<SimpleGrantedAuthority> simpleAuthorities) {
		this.simpleAuthorities = simpleAuthorities;
	}

	public boolean containsAdminRole() {
		return contains("ROLE_ADMIN");
	}

	public boolean contains(String auth) {
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(auth);
		for (var element : this.simpleAuthorities) {
			if (element.equals(authority))
				return true;
		}
		return false;
	}
}
