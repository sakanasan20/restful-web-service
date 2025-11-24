package com.niqdev.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.niqdev.web.dto.request.AddressCreateDto;
import com.niqdev.web.dto.response.AddressResponseDto;
import com.niqdev.web.entity.AddressEntity;
import com.niqdev.web.entity.UserEntity;
import com.niqdev.web.model.AddressModel;

@Mapper(componentModel = "spring")
public interface AddressMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "userId", ignore = true)
	@Mapping(target = "addressId", expression = "java(java.util.UUID.randomUUID().toString())")
	AddressModel toModel(AddressCreateDto dto);
	
	AddressResponseDto toDto(AddressModel model);
	
	@Mapping(target = "user", expression = "java(userIdToEntity(model.getUserId()))")
	AddressEntity toEntity(AddressModel model);
	
	@Mapping(target = "userId", source = "user.userId")
	AddressModel toModel(AddressEntity entity);
	
    default UserEntity userIdToEntity(String userId) {
        if (userId == null) return null;
        UserEntity user = new UserEntity();
        user.setUserId(userId);
        return user;
    }
	
}
