package com.niqdev.web.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.niqdev.web.entity.UserEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecurityUser implements UserDetails {

	private static final long serialVersionUID = 7852072150776749056L;
	
	private String userId;
	
    private String username;
    
    private String password;
    
    private List<GrantedAuthority> authorities = new ArrayList<>();
	
    public SecurityUser(UserEntity entity) {
        this.userId = entity.getUserId();
        this.username = entity.getEmail();
        this.password = entity.getEncryptedPassword();
    }
    
}
