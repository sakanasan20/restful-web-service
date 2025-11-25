package com.niqdev.web.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum AddressServiceErrors {

	MISSING_REQUIRED_FIELD("Missing required field. Please check documentation for required fields", HttpStatus.BAD_REQUEST), 
	RECORD_ALREADY_EXISTS("Record already exists", HttpStatus.BAD_REQUEST), 
	INTERNAL_SERVER_ERROR("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR), 
	NO_RECORD_FOUND("Record with provided id is not found", HttpStatus.NOT_FOUND), 
	AUTHENTICATION_FAILED("Authentication failed", HttpStatus.UNAUTHORIZED), 
	COULD_NOT_UPDATE_RECORD("Could not update record", HttpStatus.BAD_REQUEST), 
	COULD_NOT_DELETE_RECORD("Could not delete record", HttpStatus.BAD_REQUEST), 
	EMAIL_ADDRESS_NOT_VERIFIED("Email address could not be verified", HttpStatus.UNAUTHORIZED);

	private final String errorMessage;
	
	private final HttpStatus httpStatus;

	private AddressServiceErrors(String errorMessage, HttpStatus httpStatus) {
		this.errorMessage = errorMessage;
		this.httpStatus = httpStatus;
	}

}
