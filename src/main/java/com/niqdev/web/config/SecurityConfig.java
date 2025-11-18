package com.niqdev.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.niqdev.web.filter.AuthenticationFilter;
import com.niqdev.web.filter.AuthorizationFilter;
import com.niqdev.web.service.UserService;

@Configuration
public class SecurityConfig {

    private final SecurityProperties props;
	
	public SecurityConfig(SecurityProperties props) {
		this.props = props;
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationManager authenticationManager(
	        AuthenticationConfiguration authenticationConfiguration
	) throws Exception {
	    return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	AuthenticationFilter authenticationFilter(AuthenticationManager authenticationManager, UserService userService) {
		AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager, userService, props);
		authenticationFilter.setFilterProcessesUrl(props.getApiLoginUrl());
		return authenticationFilter;
	}
	
	@Bean
	AuthorizationFilter authorizationFilter(AuthenticationManager authenticationManager) {
		return new AuthorizationFilter(authenticationManager, props);
	}

	@Bean
	SecurityFilterChain configure(
			HttpSecurity http, 
			AuthenticationFilter authenticationFilter, 
			AuthorizationFilter authorizationFilter
		) throws Exception {
		
		http
			.csrf(csrf -> csrf.disable())
			.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
			.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(
					"/swagger-ui/**",  
		            "/v3/api-docs/**", 
		            "/h2/**", 
		            "/api/login"
			    ).permitAll()
				.requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
				.anyRequest().authenticated()
			)
			.addFilter(authenticationFilter)
			.addFilter(authorizationFilter);
			
		return http.build();
	}

}
