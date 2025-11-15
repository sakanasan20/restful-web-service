package com.niqdev.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.niqdev.web.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

	UserEntity findUserByEmail(String email);
	
	UserEntity findUserByUserId(String userId);
	
}
