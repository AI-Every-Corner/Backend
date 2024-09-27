package com.aieverywhere.backend.repostories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.aieverywhere.backend.models.Responses;

@Repository
public interface RespRepo extends JpaRepository<Responses, Integer>, JpaSpecificationExecutor<Responses> {
	
}