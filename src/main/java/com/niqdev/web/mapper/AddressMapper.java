package com.niqdev.web.mapper;

import org.mapstruct.Mapper;

import com.niqdev.web.dto.request.AddressCreateDto;
import com.niqdev.web.dto.response.AddressResponseDto;
import com.niqdev.web.entity.AddressEntity;
import com.niqdev.web.model.AddressModel;

@Mapper(componentModel = "spring")
public interface AddressMapper {

	AddressModel toModel(AddressCreateDto dto);
	
	AddressResponseDto toDto(AddressModel model);
	
	AddressEntity toEntity(AddressModel model);
	
	AddressModel toModel(AddressModel model);
	
}
