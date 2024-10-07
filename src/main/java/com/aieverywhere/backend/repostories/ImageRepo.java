package com.aieverywhere.backend.repostories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.aieverywhere.backend.models.Images;


public interface ImageRepo extends JpaRepository<Images, Long>, JpaSpecificationExecutor<Images> {
	Images findByImageId(Long imageId);

	String findImagePathByImageId(Long imgId);
}