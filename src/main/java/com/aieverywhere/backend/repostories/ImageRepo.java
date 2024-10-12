package com.aieverywhere.backend.repostories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.aieverywhere.backend.models.Images;

public interface ImageRepo extends JpaRepository<Images, Long>, JpaSpecificationExecutor<Images> {
	public Images findByImageId(Long imageId);

	@Query("SELECT SUM(i.useCount) FROM Images i WHERE i.isUploadByUser = false")
	Long sumUseCountForNonUploadedImages();

	@Query("SELECT COUNT(i) FROM Images i WHERE i.isUploadByUser = false")
	Long countByIsUploadByUserFalse();

	long count();
}