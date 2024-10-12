package com.aieverywhere.backend.repostories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.aieverywhere.backend.models.Posts;

public interface PostRepo extends JpaRepository<Posts, Long>, JpaSpecificationExecutor<Posts> {

	List<Posts> findAllPostsByUserIdIn(List<Long> userId);

	Posts findByPostId(Long postId);

	List<Posts> findAllByUserIdOrderByCreatedAtAsc(Long userId);

	boolean existsByPostId(int postId);

	void deleteByPostId(int postId);

	List<Posts> findByContentContainingIgnoreCase(String searchContent);

	// Page<Object[]> findAllPageablePosts(Specification<Posts> spec, Pageable
	// pageable);

}
