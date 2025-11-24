package com.niqdev.web.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.niqdev.web.dto.request.UserCreateDto;
import com.niqdev.web.dto.request.UserUpdateDto;
import com.niqdev.web.dto.response.OperationResponseDto;
import com.niqdev.web.dto.response.UserResponseDto;
import com.niqdev.web.entity.UserEntity;
import com.niqdev.web.exception.OperationNames;
import com.niqdev.web.exception.OperationStatuses;
import com.niqdev.web.exception.UserServiceErrors;
import com.niqdev.web.exception.UserServiceException;
import com.niqdev.web.mapper.UserMapper;
import com.niqdev.web.model.UserModel;
import com.niqdev.web.repository.UserRepository;
import com.niqdev.web.security.SecurityUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserEntity userEntity = userRepository.findByEmail(username);
		
		if (userEntity == null) {
			throw new UserServiceException(UserServiceErrors.NO_RECORD_FOUND);
		}
		
		return new SecurityUser(userEntity);
	}
	
	@Transactional
	@Override
	public UserResponseDto createUser(UserCreateDto userCreateDto) {

		if (userRepository.findByEmail(userCreateDto.getEmail()) != null) {
			throw new UserServiceException(UserServiceErrors.RECORD_ALREADY_EXISTS);
		}
		
		UserModel userModel = userMapper.toModel(userCreateDto);
		
		userModel.setUserId(UUID.randomUUID().toString());
		
		userModel.setEncryptedPassword(passwordEncoder.encode(userCreateDto.getPassword()));
		
		UserEntity userEntity = userRepository.save(userMapper.toEntity(userModel));
		
		return userMapper.toDto(userMapper.toModel(userEntity));
	}

	@Transactional(readOnly = true)
	@Override
	public UserResponseDto getUserByUserId(String userId) {
		
		UserEntity userEntity = userRepository.findDetailByUserId(userId);
		
		if (userEntity == null) {
			throw new UserServiceException(UserServiceErrors.NO_RECORD_FOUND);
		}
		
		log.info(userEntity.toString());
		
		UserModel userModel = userMapper.toModel(userEntity);
		
		log.info(userModel.toString());
		
		return userMapper.toDto(userModel);
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<UserResponseDto> getUsers(int page, int limit) {
		
		if (page > 0) {
			page = page - 1;
		}
		
		Pageable pageable = PageRequest.of(page, limit);
		
		Page<UserEntity> userEntities = userRepository.findAllWithDetail(pageable);
		
		return userEntities.stream()
				.map(userMapper::toModel)
				.map(userMapper::toDto)
				.collect(Collectors.toList());
	}

	@Transactional
	@Override
	public UserResponseDto updateUser(String userId, UserUpdateDto userUpdateDto) {
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if (userEntity == null) {
			throw new UserServiceException(UserServiceErrors.NO_RECORD_FOUND);
		}
		
		userEntity.setFirstName(userUpdateDto.getFirstName());
		userEntity.setLastName(userUpdateDto.getLastName());
		
		return userMapper.toDto(userMapper.toModel(userRepository.save(userEntity)));
	}

	@Transactional
	@Override
	public OperationResponseDto deleteUser(String userId) {
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if (userEntity == null) {
			throw new UserServiceException(UserServiceErrors.NO_RECORD_FOUND);
		}
		
		userRepository.delete(userEntity);
		
		OperationResponseDto operationResponseDto = new OperationResponseDto();
		
		operationResponseDto.setName(OperationNames.DELETE.name());
		operationResponseDto.setResult(OperationStatuses.SUCCESS.name());
		
		return operationResponseDto;
	}

}
