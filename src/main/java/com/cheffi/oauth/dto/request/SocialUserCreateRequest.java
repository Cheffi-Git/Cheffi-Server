package com.cheffi.oauth.dto.request;

import java.util.List;

import com.cheffi.user.constant.Password;
import com.cheffi.user.constant.UserType;
import com.cheffi.user.domain.Role;
import com.cheffi.user.dto.UserCreateRequest;

public class SocialUserCreateRequest implements UserCreateRequest {

	private final String name;
	private final String email;

	private final UserType userType;
	private final List<Role> roles;

	public SocialUserCreateRequest(String email, String name, UserType userType, List<Role> roles) {
		this.email = email;
		this.name = name;
		this.userType = userType;
		this.roles = roles;
	}

	@Override
	public String email() {
		return email;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public UserType userType() {
		return userType;
	}

	@Override
	public List<Role> roles() {
		return roles;
	}

	@Override
	public Password password() {
		return null;
	}
}
