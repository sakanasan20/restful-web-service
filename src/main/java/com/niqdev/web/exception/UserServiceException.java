package com.niqdev.web.exception;

public class UserServiceException extends RuntimeException {

	private static final long serialVersionUID = -5561279567016752289L;

	private UserServiceErrors userServiceErrors;
	
	public UserServiceException(UserServiceErrors userServiceErrors) {
		super(userServiceErrors.getErrorMessage());
		this.userServiceErrors = userServiceErrors;
	}

	public UserServiceErrors getUserServiceErrors() {
		return userServiceErrors;
	}

	public void setUserServiceErrors(UserServiceErrors userServiceErrors) {
		this.userServiceErrors = userServiceErrors;
	}

}
