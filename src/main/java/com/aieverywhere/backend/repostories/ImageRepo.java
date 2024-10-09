package com.aieverywhere.backend.repostories;

import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.aieverywhere.backend.models.Images;
import java.util.List;



public interface ImageRepo extends JpaRepository<Images, Long>, JpaSpecificationExecutor<Images> {
	public Images findByImgId(Long imgId);
}