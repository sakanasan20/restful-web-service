package com.niqdev.web.dto.response;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDto implements Serializable {

	private static final long serialVersionUID = -3457940065204974281L;

	private Date timestamp;
	
	private String message;
	
}
