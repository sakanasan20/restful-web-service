package com.niqdev.web.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.niqdev.web.dto.response.AddressResponseDto;
import com.niqdev.web.entity.AddressEntity;
import com.niqdev.web.exception.AddressServiceErrors;
import com.niqdev.web.exception.AddressServiceException;
import com.niqdev.web.mapper.AddressMapper;
import com.niqdev.web.repository.AddressRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AddressServiceImpl implements AddressService {

	private final AddressRepository addressRepository;
	private final AddressMapper addressMapper;
	
	@Override
	public List<AddressResponseDto> getByUserId(String userId) {
		
		List<AddressEntity> addressEntities = addressRepository.findDetailByUser_UserId(userId);
		
		return addressEntities.stream()
				.map(addressMapper::toModel)
				.map(addressMapper::toDto)
				.toList();
	}

	@Override
	public AddressResponseDto getByUserIdAndAddressId(String userId, String addressId) {
		
		AddressEntity addressEntity = addressRepository.findByUser_UserIdAndAddressId(userId, addressId);
		
		if (addressEntity == null) {
			throw new AddressServiceException(AddressServiceErrors.NO_RECORD_FOUND);
		}
		
		return addressMapper.toDto(addressMapper.toModel(addressEntity));
	}
	
}
