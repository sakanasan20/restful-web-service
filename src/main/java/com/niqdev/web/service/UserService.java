package com.niqdev.web.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.niqdev.web.dto.request.UserCreateDto;
import com.niqdev.web.dto.request.UserUpdateDto;
import com.niqdev.web.dto.response.OperationResponseDto;
import com.niqdev.web.dto.response.UserResponseDto;
import com.niqdev.web.model.UserModel;

public interface UserService extends UserDetailsService {

	UserResponseDto createUser(UserCreateDto userCreateDto);

	UserResponseDto getUserByUserId(String userId);
	
	List<UserResponseDto> getUsers(int page, int limit);
	
	UserResponseDto updateUser(String userId, UserUpdateDto userUpdateDto);

	OperationResponseDto deleteUser(String userId);

	UserModel getUserByEmailInternal(String email);
	
}
