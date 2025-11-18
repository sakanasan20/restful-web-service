package com.niqdev.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "web")
public class SecurityProperties {

    private long expirationTime;
    
    private String apiLoginUrl;
    
    private String tokenSecret;

	public long getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(long expirationTime) {
		this.expirationTime = expirationTime;
	}

	public String getApiLoginUrl() {
		return apiLoginUrl;
	}

	public void setApiLoginUrl(String apiLoginUrl) {
		this.apiLoginUrl = apiLoginUrl;
	}

	public String getTokenSecret() {
		return tokenSecret;
	}

	public void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
	}

}
