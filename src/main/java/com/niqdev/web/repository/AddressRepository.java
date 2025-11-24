package com.niqdev.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.niqdev.web.entity.AddressEntity;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {

	@EntityGraph(attributePaths = {"user"})
	List<AddressEntity> findDetailByUser_UserId(String userId);

	@EntityGraph(attributePaths = {"user"})
	AddressEntity findByUser_UserIdAndAddressId(String userId, String addressId);

}
