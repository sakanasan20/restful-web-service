package com.niqdev.web.service;

import java.util.List;

import com.niqdev.web.dto.response.AddressResponseDto;

public interface AddressService {

	List<AddressResponseDto> getByUserId(String userId);

	AddressResponseDto getByUserIdAndAddressId(String userId, String addressId);

}
