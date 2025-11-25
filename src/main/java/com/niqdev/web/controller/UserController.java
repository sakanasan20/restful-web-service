package com.niqdev.web.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.niqdev.web.assembler.AddressModelAssembler;
import com.niqdev.web.assembler.UserModelAssembler;
import com.niqdev.web.dto.request.UserCreateDto;
import com.niqdev.web.dto.request.UserUpdateDto;
import com.niqdev.web.dto.response.AddressResponseDto;
import com.niqdev.web.dto.response.OperationResponseDto;
import com.niqdev.web.dto.response.UserResponseDto;
import com.niqdev.web.exception.UserServiceErrors;
import com.niqdev.web.exception.UserServiceException;
import com.niqdev.web.service.AddressService;
import com.niqdev.web.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	private final UserService userService;
	private final UserModelAssembler userModelAssembler;
	private final AddressService addressService;
	private final AddressModelAssembler addressModelAssembler;
	
	@ResponseStatus(code = HttpStatus.CREATED)
	@PostMapping(
			consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public EntityModel<UserResponseDto> createUser(
			@RequestBody UserCreateDto userCreateDto) {
		if (userCreateDto.getFirstName().isBlank()) {
			throw new UserServiceException(UserServiceErrors.MISSING_REQUIRED_FIELD);
		}
		return userModelAssembler.toModel(userService.createUser(userCreateDto));
	}
	
	@GetMapping(
			path = "/{userId}", 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public EntityModel<UserResponseDto> getUser(
			@PathVariable(name = "userId") String userId) {
		return userModelAssembler.toModel(userService.getUserByUserId(userId));
	}
	
	@GetMapping(
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public PagedModel<EntityModel<UserResponseDto>> getUsers(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "25") int limit) {
		return userModelAssembler.toPagedModel(userService.getUsers(page, limit));
	}
	
	@PutMapping(
			path = "/{userId}", 
			consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public EntityModel<UserResponseDto> updateUser(
			@PathVariable(name = "userId") String userId, 
			@RequestBody UserUpdateDto userUpdateDto) {
		return userModelAssembler.toModel(userService.updateUser(userId, userUpdateDto));
	}
	
	@DeleteMapping(
			path = "/{userId}",
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public OperationResponseDto deleteUser(
			@PathVariable(name = "userId") String userId) {
		return userService.deleteUser(userId);
	}
	
	@GetMapping(
			path = "/{userId}/addresses", 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public CollectionModel<EntityModel<AddressResponseDto>> getAddresses(
			@PathVariable(name = "userId") String userId) {
	    return addressModelAssembler.toCollectionModel(addressService.getByUserId(userId));
	}
	
	@GetMapping(
			path = "/{userId}/addresses/{addressId}", 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public EntityModel<AddressResponseDto> getAddress(
			@PathVariable(name = "userId") String userId, 
			@PathVariable(name = "addressId") String addressId) {
		return addressModelAssembler.toModel(addressService.getByUserIdAndAddressId(userId, addressId));
	}
	
}
