package com.niqdev.web.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import com.niqdev.web.dto.request.UserCreateDto;
import com.niqdev.web.dto.response.UserResponseDto;
import com.niqdev.web.entity.UserEntity;
import com.niqdev.web.model.UserModel;

@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface UserMapper {

	@Mappings({
		@Mapping(target = "id", ignore = true), 
		@Mapping(target = "userId", ignore = true), 
		@Mapping(target = "encryptedPassword", ignore = true), 
		@Mapping(target = "emailVerificationStatus", ignore = true), 
		@Mapping(target = "emailVerificationToken", ignore = true), 
		@Mapping(target = "addresses", source = "addresses")
	})
	UserModel toModel(UserCreateDto dto);
	
	@Mapping(target = "addresses", source = "addresses")
	UserEntity toEntity(UserModel model);
	
	@Mapping(target = "addresses", source = "addresses")
	UserModel toModel(UserEntity entity);
	
	@Mapping(target = "addresses", source = "addresses")
	UserResponseDto toDto(UserModel model);
	
    @AfterMapping
    default void setUserForAddresses(UserModel model, @MappingTarget UserEntity entity) {
        if (entity.getAddresses() != null) {
        	entity.getAddresses().forEach(addr -> addr.setUser(entity));
        }
    }
	
}
