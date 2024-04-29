package com.cheffi.common.config.exception.business;

import com.cheffi.common.code.ErrorCode;

public class GetLockFailedException extends BusinessException{
	public GetLockFailedException(ErrorCode errorCode) {
		super(errorCode);
	}
}
