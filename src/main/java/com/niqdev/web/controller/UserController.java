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
import com.niqdev.web.model.UserRequestModel;
import com.niqdev.web.model.UserResponseModel;
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
	public UserResponseModel getUser(@PathVariable(name = "userId") String userId) {
		
		UserResponseModel userResponseModel = new UserResponseModel();
		
		UserDto foundUser = userService.getUserByUserId(userId);
		
		BeanUtils.copyProperties(foundUser, userResponseModel);
		
		return userResponseModel;
	}
	
	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserResponseModel createUser(@RequestBody UserRequestModel userRequestModel) {
		
		UserResponseModel userResponseModel = new UserResponseModel();
		
		UserDto userDto = new UserDto();
		
		BeanUtils.copyProperties(userRequestModel, userDto);
		
		UserDto createdUser = userService.createUser(userDto);
		
		BeanUtils.copyProperties(createdUser, userResponseModel);
		
		return userResponseModel;
	}
	
	@PutMapping
	public String updateUser() {
		return "Update User";
	}
	
	@DeleteMapping
	public String deleteUser() {
		return "Delete User";
	}
	
}
