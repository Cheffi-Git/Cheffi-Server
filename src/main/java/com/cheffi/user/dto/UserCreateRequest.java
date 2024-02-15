package com.cheffi.user.dto;

import java.util.List;

import com.cheffi.oauth.dto.request.SocialUserCreateRequest;
import com.cheffi.user.constant.Password;
import com.cheffi.user.constant.UserType;
import com.cheffi.user.domain.Role;

public interface UserCreateRequest {
	String email();

	String name();

	UserType userType();

	List<Role> roles();

	Password password();

	static UserCreateRequest ofSocial(String email, String name, UserType userType, List<Role> roles) {
		return new SocialUserCreateRequest(email, name, userType, roles);
	}

}
