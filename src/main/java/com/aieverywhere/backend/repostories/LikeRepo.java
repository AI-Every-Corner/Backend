package com.aieverywhere.backend.repostories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.aieverywhere.backend.models.Likes;

public interface LikeRepo extends JpaRepository<Likes, Long>, JpaSpecificationExecutor<Likes> {

	void deleteAllByLikeId(Long likeId);
	
}
