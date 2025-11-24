package com.niqdev.web.dto.request;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDto implements Serializable {

	private static final long serialVersionUID = -3799650783752012552L;

	private String firstName;
	
	private String lastName;
	
	private List<AddressUpdateDto> addresses;
	
}
