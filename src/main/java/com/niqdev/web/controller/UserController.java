package com.niqdev.web.controller;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.niqdev.web.dto.UserDto;
import com.niqdev.web.exception.OperationNames;
import com.niqdev.web.exception.OperationStatuses;
import com.niqdev.web.exception.UserServiceErrors;
import com.niqdev.web.exception.UserServiceException;
import com.niqdev.web.model.request.UserCreateModel;
import com.niqdev.web.model.request.UserUpdateModel;
import com.niqdev.web.model.response.OperationStatusModel;
import com.niqdev.web.model.response.UserModel;
import com.niqdev.web.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
	
	@Autowired
	private UserService userService;

	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public List<UserModel> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "25") int limit) {
		
		List<UserModel> userModels = new ArrayList<>();
		
		List<UserDto> usersFound = userService.getUsers(page, limit);
		
		for (UserDto userFound : usersFound) {
			UserModel userModel = new UserModel();
			BeanUtils.copyProperties(userFound, userModel);
			userModels.add(userModel);
		}
		
		return userModels;
	}
	
	@GetMapping(path = "/{userId}", 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserModel getUser(@PathVariable(name = "userId") String userId) {
		
		UserModel userModel = new UserModel();
		
		UserDto userFound = userService.getUserByUserId(userId);
		
		BeanUtils.copyProperties(userFound, userModel);
		
		return userModel;
	}
	
	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserModel createUser(@RequestBody UserCreateModel user) {
		
		if (user.getFirstName().isBlank()) {
			throw new UserServiceException(UserServiceErrors.MISSING_REQUIRED_FIELD);
		}
		
		UserModel userModel = new UserModel();
		
		UserDto userDto = new UserDto();
		
		BeanUtils.copyProperties(user, userDto);
		
		UserDto createdUser = userService.createUser(userDto);
		
		BeanUtils.copyProperties(createdUser, userModel);
		
		return userModel;
	}
	
	@PutMapping(path = "/{userId}", 
			consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserModel updateUser(@PathVariable(name = "userId") String userId, 
			@RequestBody UserUpdateModel user) {
		
		UserModel userModel = new UserModel();
		
		UserDto userDto = new UserDto();
		
		BeanUtils.copyProperties(user, userDto);
		
		UserDto updatedUser = userService.updateUser(userId, userDto);
		
		BeanUtils.copyProperties(updatedUser, userModel);
		
		return userModel;
	}
	
	@DeleteMapping(path = "/{userId}")
	public OperationStatusModel deleteUser(@PathVariable(name = "userId") String userId) {
		
		userService.deleteUser(userId);
		
		OperationStatusModel operationStatus = new OperationStatusModel();
		
		operationStatus.setOperationName(OperationNames.DELETE.name());
		operationStatus.setOperationResult(OperationStatuses.SUCCESS.name());
		
		return operationStatus;
	}
	
}
