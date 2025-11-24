package com.niqdev.web.dto.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OperationResponseDto implements Serializable {

	private static final long serialVersionUID = 5065429299355309924L;

	private String result;
	
	private String name;
	
}
