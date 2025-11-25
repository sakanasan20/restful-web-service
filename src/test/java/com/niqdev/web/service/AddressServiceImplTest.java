package com.niqdev.web.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.niqdev.web.dto.response.AddressResponseDto;
import com.niqdev.web.entity.AddressEntity;
import com.niqdev.web.exception.AddressServiceErrors;
import com.niqdev.web.exception.AddressServiceException;
import com.niqdev.web.mapper.AddressMapper;
import com.niqdev.web.model.AddressModel;
import com.niqdev.web.repository.AddressRepository;

class AddressServiceImplTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private AddressServiceImpl addressService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /* ============================================================
     * getByUserId
     * ============================================================ */
    @Test
    void testGetByUserId_Success() {
        String userId = "u1";

        AddressEntity entity1 = new AddressEntity();
        AddressEntity entity2 = new AddressEntity();

        List<AddressEntity> entityList = List.of(entity1, entity2);

        AddressModel model1 = new AddressModel();
        AddressModel model2 = new AddressModel();

        AddressResponseDto dto1 = new AddressResponseDto();
        AddressResponseDto dto2 = new AddressResponseDto();

        when(addressRepository.findDetailByUser_UserId(userId)).thenReturn(entityList);
        when(addressMapper.toModel(entity1)).thenReturn(model1);
        when(addressMapper.toModel(entity2)).thenReturn(model2);
        when(addressMapper.toDto(model1)).thenReturn(dto1);
        when(addressMapper.toDto(model2)).thenReturn(dto2);

        List<AddressResponseDto> result = addressService.getByUserId(userId);

        assertEquals(2, result.size());
        verify(addressRepository).findDetailByUser_UserId(userId);
        verify(addressMapper, times(2)).toModel(any(AddressEntity.class));
        verify(addressMapper, times(2)).toDto(any());
    }

    @Test
    void testGetByUserId_EmptyList() {
        when(addressRepository.findDetailByUser_UserId("u1")).thenReturn(Collections.emptyList());

        List<AddressResponseDto> result = addressService.getByUserId("u1");

        assertTrue(result.isEmpty());
        verify(addressRepository).findDetailByUser_UserId("u1");
        verifyNoMoreInteractions(addressMapper); // mapper 不該被呼叫
    }


    /* ============================================================
     * getByUserIdAndAddressId
     * ============================================================ */
    @Test
    void testGetByUserIdAndAddressId_Success() {
        String userId = "u1";
        String addressId = "a1";

        AddressEntity entity = new AddressEntity();
        AddressModel model = new AddressModel();
        AddressResponseDto dto = new AddressResponseDto();

        when(addressRepository.findByUser_UserIdAndAddressId(userId, addressId)).thenReturn(entity);
        when(addressMapper.toModel(entity)).thenReturn(model);
        when(addressMapper.toDto(model)).thenReturn(dto);

        AddressResponseDto result = addressService.getByUserIdAndAddressId(userId, addressId);

        assertNotNull(result);
        verify(addressRepository).findByUser_UserIdAndAddressId(userId, addressId);
        verify(addressMapper).toModel(entity);
        verify(addressMapper).toDto(model);
    }

    @Test
    void testGetByUserIdAndAddressId_NotFound_ReturnsNullOrThrows() {
        when(addressRepository.findByUser_UserIdAndAddressId("u", "a")).thenReturn(null);

        // 根據你 service 的實作，它會把 null 丟給 mapper
        // 如果你 mapper 處理 null → 正常
        // 如果 mapper 不允許 null → 可能丟 NullPointerException

        AddressServiceException exception = assertThrows(AddressServiceException.class,
                () -> addressService.getByUserIdAndAddressId("u", "a"));

        assertEquals(AddressServiceErrors.NO_RECORD_FOUND, exception.getAddressServiceErrors());
        verify(addressRepository, times(1)).findByUser_UserIdAndAddressId(anyString(), anyString());
        verifyNoInteractions(addressMapper);
    }

}
