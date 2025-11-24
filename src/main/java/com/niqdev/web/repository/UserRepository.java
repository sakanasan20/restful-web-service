package com.niqdev.web.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.niqdev.web.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

	UserEntity findByEmail(String email);
	
	@EntityGraph(attributePaths = {"addresses"})
	UserEntity findDetailByEmail(String email);
	
	UserEntity findByUserId(String userId);
	
	@EntityGraph(attributePaths = {"addresses"})
	UserEntity findDetailByUserId(String userId);
	
	Page<UserEntity> findAll(Pageable pageable);
	
	@EntityGraph(attributePaths = {"addresses"})
	@Query("select u from UserEntity u")
	Page<UserEntity> findAllWithDetail(Pageable pageable);
	
}
