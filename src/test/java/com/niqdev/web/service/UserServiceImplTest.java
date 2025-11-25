package com.niqdev.web.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

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

class UserServiceImplTest {

	@Mock
	UserRepository userRepository;
	
	@Mock
	UserMapper userMapper;
	
	@Mock
	PasswordEncoder passwordEncoder;
	
	@InjectMocks
	UserServiceImpl userService;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	/* ==================================================
	 * loadUserByUsername
	 * ================================================== */
	
	@Test
	void testLoadUserByUsername_Success() {
		String email = "test@abc.com";
		UserEntity entity = new UserEntity();
		entity.setEmail(email);

		when(userRepository.findByEmail(email)).thenReturn(entity);

		SecurityUser securityUser = (SecurityUser) userService.loadUserByUsername(email);

		assertNotNull(securityUser);
		assertEquals(email, securityUser.getUsername());
	}

	@Test
	void testLoadUserByUsername_NotFound() {
		String email = "test@abc.com";

		when(userRepository.findByEmail(email)).thenReturn(null);

		UserServiceException exception = assertThrows(UserServiceException.class, 
				() -> userService.loadUserByUsername(email));
		
        assertEquals(UserServiceErrors.NO_RECORD_FOUND, exception.getUserServiceErrors());
        verify(userRepository, times(1)).findByEmail(email);
	}

	/* ==================================================
	 * createUser
	 * ================================================== */
	
	@Test
	void testCreateUser_Success() {
		UserCreateDto dto = new UserCreateDto();
		dto.setEmail("a@a.com");
		dto.setPassword("123456");

		UserModel model = new UserModel();
		model.setEmail("a@a.com");

		UserEntity savedEntity = new UserEntity();
		savedEntity.setUserId("uuid");

		when(userRepository.findByEmail(dto.getEmail())).thenReturn(null);
		when(userMapper.toModel(dto)).thenReturn(model);
		when(passwordEncoder.encode(dto.getPassword())).thenReturn("ENC_PASS");
		when(userMapper.toEntity(model)).thenReturn(savedEntity);
		when(userRepository.save(savedEntity)).thenReturn(savedEntity);
		when(userMapper.toModel(savedEntity)).thenReturn(model);
		when(userMapper.toDto(model)).thenReturn(new UserResponseDto());

		UserResponseDto response = userService.createUser(dto);

		assertNotNull(response);
		verify(userRepository).save(any());
	}

	@Test
	void testCreateUser_EmailExists() {
		UserCreateDto dto = new UserCreateDto();
		dto.setEmail("a@a.com");

		when(userRepository.findByEmail("a@a.com")).thenReturn(new UserEntity());

		UserServiceException exception = assertThrows(UserServiceException.class, 
				() -> userService.createUser(dto));
		
		assertEquals(UserServiceErrors.RECORD_ALREADY_EXISTS, exception.getUserServiceErrors());
        verify(userRepository, times(1)).findByEmail(anyString());
        verifyNoInteractions(userMapper);
	}
	
    /* ==================================================
	 * getUserByUserId
	 * ================================================== */
	
    @Test
    void testGetUserByUserId_Success() {
        // Arrange
        String userId = "123";
        UserEntity entity = new UserEntity();
        entity.setUserId(userId);
        UserModel model = new UserModel();
        model.setUserId(userId);
        UserResponseDto dto = new UserResponseDto();
        dto.setUserId(userId);

        when(userRepository.findDetailByUserId(userId)).thenReturn(entity);
        when(userMapper.toModel(entity)).thenReturn(model);
        when(userMapper.toDto(model)).thenReturn(dto);

        // Act
        UserResponseDto result = userService.getUserByUserId(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        verify(userRepository, times(1)).findDetailByUserId(userId);
        verify(userMapper, times(1)).toModel(entity);
        verify(userMapper, times(1)).toDto(model);
    }

    @Test
    void testGetUserByUserId_NotFound() {
        // Arrange
        String userId = "123";
        when(userRepository.findDetailByUserId(userId)).thenReturn(null);

        // Act & Assert
        UserServiceException exception = assertThrows(UserServiceException.class,
                () -> userService.getUserByUserId(userId));

        assertEquals(UserServiceErrors.NO_RECORD_FOUND, exception.getUserServiceErrors());
        verify(userRepository, times(1)).findDetailByUserId(userId);
        verifyNoInteractions(userMapper);
    }
    
    /* ==================================================
	 * getUsers
	 * ================================================== */
    
	@Test
	void testGetUsers_Success() {
		Pageable pageable = PageRequest.of(0, 10);

		List<UserEntity> list = new ArrayList<>();
		list.add(new UserEntity());

		Page<UserEntity> page = new PageImpl<>(list, pageable, 1);

		when(userRepository.findAllWithDetail(any())).thenReturn(page);
		when(userMapper.toDtoPage(page)).thenReturn(Page.empty());

		Page<UserResponseDto> result = userService.getUsers(1, 10);

		assertNotNull(result);
		verify(userRepository).findAllWithDetail(any());
	}
    
    /* ==================================================
	 * updateUser
	 * ================================================== */
    
    @Test
    void testUpdateUser_Success() {
        String userId = "123";
        UserUpdateDto updateDto = new UserUpdateDto();
        updateDto.setFirstName("Nick");
        updateDto.setLastName("Chen");

        UserEntity entity = new UserEntity();
        entity.setUserId(userId);

        UserModel model = new UserModel();

        when(userRepository.findByUserId(userId)).thenReturn(entity);
        when(userRepository.save(entity)).thenReturn(entity);
        when(userMapper.toModel(entity)).thenReturn(model);
        when(userMapper.toDto(model)).thenReturn(new UserResponseDto());

        UserResponseDto response = userService.updateUser(userId, updateDto);

        assertNotNull(response);
        assertEquals("Nick", entity.getFirstName());
        assertEquals("Chen", entity.getLastName());
    }

    @Test
    void testUpdateUser_NotFound() {
        when(userRepository.findByUserId("x")).thenReturn(null);

        UserServiceException exception = assertThrows(UserServiceException.class,
                () -> userService.updateUser("x", new UserUpdateDto()));
        
        assertEquals(UserServiceErrors.NO_RECORD_FOUND, exception.getUserServiceErrors());
        verify(userRepository, times(1)).findByUserId(anyString());
        verifyNoInteractions(userMapper);
    }
	
    /* ==================================================
	 * deleteUser
	 * ================================================== */
    
    @Test
    void testDeleteUser_Success() {
        String userId = "123";
        UserEntity entity = new UserEntity();
        entity.setUserId(userId);

        when(userRepository.findByUserId(userId)).thenReturn(entity);

        OperationResponseDto response = userService.deleteUser(userId);

        assertNotNull(response);
        assertEquals(OperationNames.DELETE.name(), response.getName());
        assertEquals(OperationStatuses.SUCCESS.name(), response.getResult());
        verify(userRepository).delete(entity);
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.findByUserId("x")).thenReturn(null);

        UserServiceException exception = assertThrows(UserServiceException.class,
                () -> userService.deleteUser("x"));
        
        assertEquals(UserServiceErrors.NO_RECORD_FOUND, exception.getUserServiceErrors());
        verify(userRepository, times(1)).findByUserId(anyString());
    }
    
}
