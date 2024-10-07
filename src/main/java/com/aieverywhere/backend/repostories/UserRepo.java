package com.aieverywhere.backend.repostories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.aieverywhere.backend.models.Users;

public interface UserRepo extends JpaRepository<Users, Long>, JpaSpecificationExecutor<Users> {
	Users findByUsername(String username);

	Users findByUserId(Long userId);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	boolean existsByPhoneNum(String phoneNum);

	long count();

	String findUsernameByUserId(Long userId);

	Users findByEmail(String email);

}