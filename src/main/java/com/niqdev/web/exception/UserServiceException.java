package com.niqdev.web.exception;

import lombok.Getter;

@Getter
public class UserServiceException extends RuntimeException {

	private static final long serialVersionUID = -5561279567016752289L;

	private final UserServiceErrors userServiceErrors;
	
	public UserServiceException(UserServiceErrors userServiceErrors) {
		super(userServiceErrors.getErrorMessage());
		this.userServiceErrors = userServiceErrors;
	}

}
