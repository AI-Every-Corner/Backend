package com.aieverywhere.backend.repostories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.aieverywhere.backend.models.Posts;

public interface PostRepo extends JpaRepository<Posts, Long>, JpaSpecificationExecutor<Posts> {
	// List<Posts> findAllPostsByUserId(List<Long> userIds);

	List<Posts> findAllPostsByUserIdIn(List<Long> userId);

	Posts findByPostId(Long postId);

	List<Posts> findAllByUserIdOrderByCreatedAtAsc(Long userId);

}
