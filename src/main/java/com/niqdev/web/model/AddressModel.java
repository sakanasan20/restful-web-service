package com.niqdev.web.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AddressModel implements Serializable {

	private static final long serialVersionUID = -7204018254310489070L;
	
	private Long id;
	
	private String addressId;

	private String street;
    
    private String city;
    
    private String postalCode;
    
    private String country;
    
    private String type;
    
    private String userId;
	
}
