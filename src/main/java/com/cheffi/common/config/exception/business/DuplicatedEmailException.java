package com.cheffi.common.config.exception.business;

import com.cheffi.common.code.ErrorCode;
import com.cheffi.user.constant.UserType;

import lombok.Getter;

@Getter
public class DuplicatedEmailException extends BusinessException {

	private final String provider;

	public DuplicatedEmailException(ErrorCode errorCode, UserType userType) {
		super(errorCode);
		this.provider = userType.toString();
	}

}
