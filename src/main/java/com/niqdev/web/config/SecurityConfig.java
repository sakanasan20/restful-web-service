package com.niqdev.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain configure(HttpSecurity http) throws Exception {

		http
			.csrf(csrf -> csrf.disable())
			.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(
					"/swagger-ui/**",  
		            "/v3/api-docs/**", 
		            "/h2/**", 
		            "/api/**"
			    ).permitAll()
				.anyRequest().authenticated()
			);
			
		return http.build();
	}

}
