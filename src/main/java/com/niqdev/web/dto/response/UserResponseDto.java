package com.niqdev.web.dto.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto implements Serializable {

	private static final long serialVersionUID = -5476248639839595341L;

	private String userId;

	private String firstName;
	
	private String lastName;
	
	private String email;
	
}
