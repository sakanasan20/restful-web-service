package com.niqdev.web.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.niqdev.web.dto.UserDto;
import com.niqdev.web.exception.UserServiceException;
import com.niqdev.web.model.request.UserCreateModel;
import com.niqdev.web.model.request.UserUpdateModel;
import com.niqdev.web.model.response.ErrorMessages;
import com.niqdev.web.model.response.UserModel;
import com.niqdev.web.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
	
	@Autowired
	private UserService userService;

	@GetMapping
	public String getUsers() {
		return "Get User";
	}
	
	@GetMapping(path = "/{userId}", 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserModel getUser(@PathVariable(name = "userId") String userId) {
		
		UserModel userResponseModel = new UserModel();
		
		UserDto foundUser = userService.getUserByUserId(userId);
		
		BeanUtils.copyProperties(foundUser, userResponseModel);
		
		return userResponseModel;
	}
	
	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserModel createUser(@RequestBody UserCreateModel user) {
		
		if (user.getFirstName().isBlank()) {
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		}
		
		UserModel userResponseModel = new UserModel();
		
		UserDto userDto = new UserDto();
		
		BeanUtils.copyProperties(user, userDto);
		
		UserDto createdUser = userService.createUser(userDto);
		
		BeanUtils.copyProperties(createdUser, userResponseModel);
		
		return userResponseModel;
	}
	
	@PutMapping(path = "/{userId}", 
			consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserModel updateUser(@PathVariable(name = "userId") String userId, 
			@RequestBody UserUpdateModel user) {
		
		UserModel userResponseModel = new UserModel();
		
		UserDto userDto = new UserDto();
		
		BeanUtils.copyProperties(user, userDto);
		
		UserDto updatedUser = userService.updateUser(userId, userDto);
		
		BeanUtils.copyProperties(updatedUser, userResponseModel);
		
		return userResponseModel;
	}
	
	@DeleteMapping
	public String deleteUser() {
		return "Delete User";
	}
	
}
