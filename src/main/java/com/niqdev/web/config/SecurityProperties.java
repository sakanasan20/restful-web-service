package com.niqdev.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "web")
public class SecurityProperties {

    private long expirationTime;
    
    private String apiLoginUrl;
    
    private String tokenSecret;

}
