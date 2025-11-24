package com.niqdev.web.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.niqdev.web.dto.request.UserCreateDto;
import com.niqdev.web.dto.request.UserUpdateDto;
import com.niqdev.web.dto.response.OperationResponseDto;
import com.niqdev.web.dto.response.UserResponseDto;
import com.niqdev.web.exception.UserServiceErrors;
import com.niqdev.web.exception.UserServiceException;
import com.niqdev.web.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	private final UserService userService;
	
	@ResponseStatus(code = HttpStatus.CREATED)
	@PostMapping(
			consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserResponseDto createUser(
			@RequestBody UserCreateDto userCreateDto) {
		if (userCreateDto.getFirstName().isBlank()) {
			throw new UserServiceException(UserServiceErrors.MISSING_REQUIRED_FIELD);
		}
		return userService.createUser(userCreateDto);
	}
	
	@GetMapping(
			path = "/{userId}", 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserResponseDto getUser(
			@PathVariable(name = "userId") String userId) {
		return userService.getUserByUserId(userId);
	}
	
	@GetMapping(
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public List<UserResponseDto> getUsers(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "25") int limit) {
		return userService.getUsers(page, limit);
	}
	
	@PutMapping(
			path = "/{userId}", 
			consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserResponseDto updateUser(
			@PathVariable(name = "userId") String userId, 
			@RequestBody UserUpdateDto userUpdateDto) {
		return userService.updateUser(userId, userUpdateDto);
	}
	
	@DeleteMapping(
			path = "/{userId}",
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public OperationResponseDto deleteUser(
			@PathVariable(name = "userId") String userId) {
		return userService.deleteUser(userId);
	}
	
}
