package com.niqdev.web.service;

import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.niqdev.web.dto.UserDto;
import com.niqdev.web.entity.UserEntity;
import com.niqdev.web.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDto createUser(UserDto userDto) {

		if (userRepository.findUserByEmail(userDto.getEmail()) != null) {
			throw new RuntimeException("Record already exists");
		}
		
		UserEntity userEntity = new UserEntity();
		
		BeanUtils.copyProperties(userDto, userEntity);
		
		userEntity.setUserId(UUID.randomUUID().toString());
		
		userEntity.setEncryptedPassword("test");
		
		UserEntity userSaved = userRepository.save(userEntity);
		
		UserDto userReturn = new UserDto();
		
		BeanUtils.copyProperties(userSaved, userReturn);
		
		return userReturn;
	}

}
