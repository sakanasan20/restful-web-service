package com.niqdev.web.exception;

import lombok.Getter;

@Getter
public class AddressServiceException extends RuntimeException {

	private static final long serialVersionUID = -5561279567016752289L;

	private final AddressServiceErrors addressServiceErrors;
	
	public AddressServiceException(AddressServiceErrors addressServiceErrors) {
		super(addressServiceErrors.getErrorMessage());
		this.addressServiceErrors = addressServiceErrors;
	}

}
