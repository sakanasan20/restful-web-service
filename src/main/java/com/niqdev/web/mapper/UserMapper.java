package com.niqdev.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.niqdev.web.dto.request.UserCreateDto;
import com.niqdev.web.dto.response.UserResponseDto;
import com.niqdev.web.entity.UserEntity;
import com.niqdev.web.model.UserModel;

@Mapper(componentModel = "spring")
public interface UserMapper {

	@Mappings({
		@Mapping(target = "id", ignore = true), 
		@Mapping(target = "userId", ignore = true), 
		@Mapping(target = "encryptedPassword", ignore = true), 
		@Mapping(target = "emailVerificationStatus", ignore = true), 
		@Mapping(target = "emailVerificationToken", ignore = true)
	})
	UserModel toModel(UserCreateDto dto);
	
	UserEntity toEntity(UserModel model);
	
	UserModel toModel(UserEntity entity);
	
	UserResponseDto toDto(UserModel model);
	
}
