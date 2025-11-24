package com.niqdev.web.dto.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginDto implements Serializable {

	private static final long serialVersionUID = 1707647316682929013L;

	private String email;
	
	private String password;

}
