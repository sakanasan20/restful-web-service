package com.niqdev.web.dto.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateDto implements Serializable {

	private static final long serialVersionUID = -3474974758965940459L;

	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String password;
	
}
