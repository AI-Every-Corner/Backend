package com.aieverywhere.backend.repostories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.aieverywhere.backend.models.Likes;
import com.aieverywhere.backend.models.Responses;

public interface RespRepo extends JpaRepository<Responses, Long>, JpaSpecificationExecutor<Responses> {

    Responses findByResponseId(Long respId);

}