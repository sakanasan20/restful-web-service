package com.niqdev.web.dto.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressCreateDto implements Serializable {

	private static final long serialVersionUID = -7138464063336750984L;

	private String street;
    
    private String city;
    
    private String postalCode;
    
    private String country;
    
    private String type;
	
}
