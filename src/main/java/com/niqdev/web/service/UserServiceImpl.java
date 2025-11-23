package com.niqdev.web.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.niqdev.web.dto.UserDto;
import com.niqdev.web.entity.UserEntity;
import com.niqdev.web.exception.UserServiceErrors;
import com.niqdev.web.exception.UserServiceException;
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
			throw new UserServiceException(UserServiceErrors.NO_RECORD_FOUND);
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
			System.out.println(UserServiceErrors.RECORD_ALREADY_EXISTS.getErrorMessage());
			System.out.println(UserServiceErrors.RECORD_ALREADY_EXISTS.getHttpStatus());
			throw new UserServiceException(UserServiceErrors.RECORD_ALREADY_EXISTS);
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
			throw new UserServiceException(UserServiceErrors.NO_RECORD_FOUND);
		}
		
		UserDto userToReturn = new UserDto();
		
		BeanUtils.copyProperties(userFound, userToReturn);
		
		return userToReturn;
	}

	@Override
	public UserDto getUserByUserId(String userId) {
		
		UserEntity userFound = userRepository.findUserByUserId(userId);
		
		if (userFound == null) {
			throw new UserServiceException(UserServiceErrors.NO_RECORD_FOUND);
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
			throw new UserServiceException(UserServiceErrors.NO_RECORD_FOUND);
		}
		
		userFound.setFirstName(userDto.getFirstName());
		userFound.setLastName(userDto.getLastName());

		UserEntity userUpdated = userRepository.save(userFound);
		
		UserDto userToReturn = new UserDto();
		
		BeanUtils.copyProperties(userUpdated, userToReturn);
		
		return userToReturn;
	}

	@Transactional
	@Override
	public void deleteUser(String userId) {
		
		UserEntity userFound = userRepository.findUserByUserId(userId);
		
		if (userFound == null) {
			throw new UserServiceException(UserServiceErrors.NO_RECORD_FOUND);
		}
		
		userRepository.delete(userFound);
	}

	@Override
	public List<UserDto> getUsers(int page, int limit) {
		
		if (page > 0) {
			page = page - 1;
		}
		
		Pageable pageable = PageRequest.of(page, limit);
		
		Page<UserEntity> usersFound = userRepository.findAll(pageable);
		
		List<UserDto> usersToReturn = new ArrayList<>();
		
		for (UserEntity userFound : usersFound) {
			
			UserDto userToReturn = new UserDto();
			
			BeanUtils.copyProperties(userFound, userToReturn);
			
			usersToReturn.add(userToReturn);
		}
		
		return usersToReturn;
	}

}
