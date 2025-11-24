package com.niqdev.web.controller;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.niqdev.web.dto.response.ErrorResponseDto;
import com.niqdev.web.exception.UserServiceException;

@ControllerAdvice
public class WebExceptionHandler {

	@ExceptionHandler(value = { UserServiceException.class })
	ResponseEntity<Object> handleUserServiceException(UserServiceException ex, WebRequest request) {
		return new ResponseEntity<>(new ErrorResponseDto(new Date(), ex.getMessage()), 
				new HttpHeaders(), 
				ex.getUserServiceErrors().getHttpStatus());
	}
	
	@ExceptionHandler(value = { Exception.class })
	ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) {
		return new ResponseEntity<>(new ErrorResponseDto(new Date(), ex.getMessage()), 
				new HttpHeaders(), 
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
