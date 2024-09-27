package com.aieverywhere.backend.repostories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.aieverywhere.backend.models.Posts;


@Repository
public interface PostRepo extends JpaRepository<Posts, Integer>, JpaSpecificationExecutor<Posts> {
	
}
