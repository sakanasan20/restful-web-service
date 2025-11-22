package com.niqdev.web.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.niqdev.web.config.SecurityConstants;
import com.niqdev.web.config.SecurityProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthorizationFilter extends BasicAuthenticationFilter {
	
	private final SecurityProperties props;

	public AuthorizationFilter(AuthenticationManager authenticationManager, SecurityProperties props) {
		super(authenticationManager);
		this.props = props;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String headerAuth = request.getHeader(SecurityConstants.HEADER_STRING);

		if (headerAuth == null || !headerAuth.startsWith(SecurityConstants.TOKEN_PRIFIX)) {
			chain.doFilter(request, response);
			return;
		}
		
		UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(request, response);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		
		String authorizationHeader = request.getHeader(SecurityConstants.HEADER_STRING);

		if (authorizationHeader == null) {
			return null;
		}
		
		String token = authorizationHeader.replace(SecurityConstants.TOKEN_PRIFIX, "");
		
		byte[] secretBytes = Base64.getEncoder().encode(props.getTokenSecret().getBytes());
		SecretKey key = Keys.hmacShaKeyFor(secretBytes);
		JwtParser jwtParser = Jwts.parser().verifyWith(key).build();
		Jws<Claims> jwsClaims = jwtParser.parseSignedClaims(token);
		Claims claims = jwsClaims.getPayload();
		String subject = claims.getSubject();

		if (subject == null) {
			return null;
		}
		
		return new UsernamePasswordAuthenticationToken(subject, null, new ArrayList<>());
	}

}
