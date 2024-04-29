package com.cheffi.common.config.exception.business;

import com.cheffi.common.code.ErrorCode;

public class ReleaseFailedException extends BusinessException{
	public ReleaseFailedException(ErrorCode errorCode) {
		super(errorCode);
	}
}
