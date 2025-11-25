package com.niqdev.web.mapper;

import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.data.domain.Page;

import com.niqdev.web.dto.request.UserCreateDto;
import com.niqdev.web.dto.response.UserResponseDto;
import com.niqdev.web.entity.UserEntity;
import com.niqdev.web.model.UserModel;

@Mapper(componentModel = "spring", uses = { AddressMapper.class })
public interface UserMapper {

	/* ===================================================== 
	 *  1. CreateDto → Model
	 * ===================================================== */
	@Mappings({ 
		@Mapping(target = "id", ignore = true), 
		@Mapping(target = "userId", ignore = true), 
		@Mapping(target = "encryptedPassword", ignore = true), 
		@Mapping(target = "emailVerificationStatus", ignore = true), 
		@Mapping(target = "emailVerificationToken", ignore = true), 
		@Mapping(target = "addresses", source = "addresses") 
	})
	UserModel toModel(UserCreateDto dto);

	/* =====================================================
	 *  2. Model ↔ Entity
	 * ===================================================== */
	@Mapping(target = "addresses", source = "addresses")
	UserEntity toEntity(UserModel model);

	@Mapping(target = "addresses", source = "addresses")
	UserModel toModel(UserEntity entity);

	/** 處理 Entity.addresses → user 的關聯設定 */
	@AfterMapping
	default void setUserToAddressList(UserModel model, @MappingTarget UserEntity entity) {
		if (entity.getAddresses() != null) {
			entity.getAddresses().forEach(addr -> addr.setUser(entity));
		}
	}

	/* =====================================================
	 *  3. Model → ResponseDTO (單筆)
	 * ===================================================== */
	@Mapping(target = "addresses", source = "addresses")
	UserResponseDto toDto(UserModel model);

	/* =====================================================
	 *  4. List / Page 映射支援
	 * ===================================================== */

	// List 支援（model → dto）
	List<UserResponseDto> toDtoList(List<UserModel> models);

	// Page 支援（entity → dto）
	default Page<UserResponseDto> toDtoPage(Page<UserEntity> entityPage) {
		return entityPage.map(entity -> toDto(toModel(entity)));
	}
	
}
