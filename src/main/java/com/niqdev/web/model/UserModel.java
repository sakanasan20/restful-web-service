package com.niqdev.web.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserModel implements Serializable {
	
	private static final long serialVersionUID = -82378767100517158L;

	private Long id;
	
	private String userId;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String password;
	
	private String encryptedPassword;
	
	private String emailVerificationToken;
	
	private Boolean emailVerificationStatus = false;

}
