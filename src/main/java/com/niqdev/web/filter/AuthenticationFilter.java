package com.niqdev.web.filter;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.niqdev.web.config.SecurityConstants;
import com.niqdev.web.config.SecurityProperties;
import com.niqdev.web.dto.UserDto;
import com.niqdev.web.model.request.UserLoginModel;
import com.niqdev.web.service.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final UserService userService;
	private final SecurityProperties props;
	
	public AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService, SecurityProperties props) {
		super(authenticationManager);
		this.userService = userService;
		this.props = props;
	}

	@Override
	public Authentication attemptAuthentication(
			HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		try {
			UserLoginModel loginRequestModel = 
					new ObjectMapper().readValue(
							request.getInputStream(), 
							UserLoginModel.class);
			
			return this.getAuthenticationManager()
					.authenticate(
							new UsernamePasswordAuthenticationToken(
									loginRequestModel.getEmail(), 
									loginRequestModel.getPassword(), 
									new ArrayList<>()));
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(
			HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) 
			throws IOException, ServletException {
		
		byte[] secretBytes = Base64.getEncoder().encode(props.getTokenSecret().getBytes());
		SecretKey key = Keys.hmacShaKeyFor(secretBytes);
		String email = ((User) authResult.getPrincipal()).getUsername();
		Instant now = Instant.now();
		UserDto userDto = userService.getUserByEmail(email);
		
		String jws = Jwts.builder()
		    .subject(email)
		    .expiration(Date.from(now.plusMillis(props.getExpirationTime())))
		    .issuedAt(Date.from(now))
		    .signWith(key, Jwts.SIG.HS512)
		    .compact();
		
		
		response.addHeader("UserId", userDto.getUserId());
		response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PRIFIX + jws);
	}

}
