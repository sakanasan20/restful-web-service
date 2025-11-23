package com.niqdev.web.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateModel {

	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String password;
	
}
