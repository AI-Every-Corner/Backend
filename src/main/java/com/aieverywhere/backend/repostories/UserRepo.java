package com.aieverywhere.backend.repostories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.aieverywhere.backend.models.Users;

@Repository
public interface UserRepo extends JpaRepository<Users, Integer>, JpaSpecificationExecutor<Users> {
	Users findByUsername(String username);
	Users findByUserId(int userid);
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
}