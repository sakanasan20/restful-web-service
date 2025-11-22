package com.niqdev.web.controller;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.niqdev.web.exception.UserServiceException;
import com.niqdev.web.model.response.ErrorMessageModel;

@ControllerAdvice
public class WebExceptionHandler {

	@ExceptionHandler(value = { UserServiceException.class })
	ResponseEntity<Object> handleUserServiceException(UserServiceException ex, 
			WebRequest request) {
		
		ErrorMessageModel errorMessageModel = 
				new ErrorMessageModel(new Date(), ex.getMessage());
		
		return new ResponseEntity<>(errorMessageModel, 
				new HttpHeaders(), 
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(value = { Exception.class })
	ResponseEntity<Object> handleAllException(Exception ex, 
			WebRequest request) {
		
		ErrorMessageModel errorMessageModel = 
				new ErrorMessageModel(new Date(), ex.getMessage());
		
		return new ResponseEntity<>(errorMessageModel, 
				new HttpHeaders(), 
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
