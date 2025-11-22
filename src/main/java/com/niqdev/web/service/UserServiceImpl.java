package com.niqdev.web.service;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.niqdev.web.dto.UserDto;
import com.niqdev.web.entity.UserEntity;
import com.niqdev.web.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserEntity userEntity = userRepository.findUserByEmail(username);
		
		if (userEntity == null) {
			throw new UsernameNotFoundException(username);
		}
		
		return new User(
				userEntity.getEmail(), 
				userEntity.getEncryptedPassword(), 
				new ArrayList<>());
	}
	
	@Override
	public UserDto createUser(UserDto userDto) {

		if (userRepository.findUserByEmail(userDto.getEmail()) != null) {
			throw new RuntimeException("Record already exists");
		}
		
		UserEntity userEntity = new UserEntity();
		
		BeanUtils.copyProperties(userDto, userEntity);
		
		userEntity.setUserId(UUID.randomUUID().toString());
		
		userEntity.setEncryptedPassword(passwordEncoder.encode(userDto.getPassword()));
		
		UserEntity userSaved = userRepository.save(userEntity);
		
		UserDto userReturn = new UserDto();
		
		BeanUtils.copyProperties(userSaved, userReturn);
		
		return userReturn;
	}

	@Override
	public UserDto getUserByEmail(String email) {
		
		UserEntity userEntity = userRepository.findUserByEmail(email);
		
		if (userEntity == null) {
			throw new UsernameNotFoundException(email);
		}
		
		UserDto userReturn = new UserDto();
		
		BeanUtils.copyProperties(userEntity, userReturn);
		
		return userReturn;
	}

	@Override
	public UserDto getUserByUserId(String userId) {
		
		UserEntity userEntity = userRepository.findUserByUserId(userId);
		
		if (userEntity == null) {
			throw new UsernameNotFoundException(userId);
		}
		
		UserDto userReturn = new UserDto();
		
		BeanUtils.copyProperties(userEntity, userReturn);
		
		return userReturn;
	}

}
