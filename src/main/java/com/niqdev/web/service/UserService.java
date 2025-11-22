package com.niqdev.web.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.niqdev.web.dto.UserDto;

public interface UserService extends UserDetailsService {

	UserDto createUser(UserDto userDto);

	UserDto getUserByEmail(String username);

	UserDto getUserByUserId(String userId);

}
