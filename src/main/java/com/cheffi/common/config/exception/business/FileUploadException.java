package com.cheffi.common.config.exception.business;

import com.cheffi.common.code.ErrorCode;

public class FileUploadException extends BusinessException {
	public FileUploadException(ErrorCode errorCode) {
		super(errorCode);
	}

	public FileUploadException(String message) {
		super(message);
	}
}
