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
import org.springframework.transaction.annotation.Transactional;

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
		
		UserEntity userFound = userRepository.findUserByEmail(username);
		
		if (userFound == null) {
			throw new UsernameNotFoundException(username);
		}
		
		return new User(
				userFound.getEmail(), 
				userFound.getEncryptedPassword(), 
				new ArrayList<>());
	}
	
	@Transactional
	@Override
	public UserDto createUser(UserDto userToCreate) {

		if (userRepository.findUserByEmail(userToCreate.getEmail()) != null) {
			throw new RuntimeException("Record already exists");
		}
		
		UserEntity userToSave = new UserEntity();
		
		BeanUtils.copyProperties(userToCreate, userToSave);
		
		userToSave.setUserId(UUID.randomUUID().toString());
		
		userToSave.setEncryptedPassword(passwordEncoder.encode(userToCreate.getPassword()));
		
		UserEntity userSaved = userRepository.save(userToSave);
		
		UserDto userToReturn = new UserDto();
		
		BeanUtils.copyProperties(userSaved, userToReturn);
		
		return userToReturn;
	}

	@Override
	public UserDto getUserByEmail(String email) {
		
		UserEntity userFound = userRepository.findUserByEmail(email);
		
		if (userFound == null) {
			throw new UsernameNotFoundException(email);
		}
		
		UserDto userToReturn = new UserDto();
		
		BeanUtils.copyProperties(userFound, userToReturn);
		
		return userToReturn;
	}

	@Override
	public UserDto getUserByUserId(String userId) {
		
		UserEntity userFound = userRepository.findUserByUserId(userId);
		
		if (userFound == null) {
			throw new UsernameNotFoundException(userId);
		}
		
		UserDto userToReturn = new UserDto();
		
		BeanUtils.copyProperties(userFound, userToReturn);
		
		return userToReturn;
	}

	@Transactional
	@Override
	public UserDto updateUser(String userId, UserDto userDto) {
		
		UserEntity userFound = userRepository.findUserByUserId(userId);
		
		if (userFound == null) {
			throw new UsernameNotFoundException(userId);
		}
		
		userFound.setFirstName(userDto.getFirstName());
		userFound.setLastName(userDto.getLastName());

		UserEntity userUpdated = userRepository.save(userFound);
		
		UserDto userToReturn = new UserDto();
		
		BeanUtils.copyProperties(userUpdated, userToReturn);
		
		return userToReturn;
	}

}
