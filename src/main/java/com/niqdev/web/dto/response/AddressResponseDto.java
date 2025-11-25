package com.niqdev.web.dto.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressResponseDto implements Serializable {

	private static final long serialVersionUID = -409403899756248293L;
	
	private String addressId;

	private String street;
    
    private String city;
    
    private String postalCode;
    
    private String country;
    
    private String type;
    
    private String userId;
	
}
