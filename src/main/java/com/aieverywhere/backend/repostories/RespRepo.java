package com.aieverywhere.backend.repostories;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.aieverywhere.backend.models.Responses;

public interface RespRepo extends JpaRepository<Responses, Long>, JpaSpecificationExecutor<Responses> {
}