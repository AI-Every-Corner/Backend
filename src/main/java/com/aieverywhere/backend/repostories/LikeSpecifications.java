package com.aieverywhere.backend.repostories;

import org.springframework.data.jpa.domain.Specification;

import com.aieverywhere.backend.models.Likes;
import com.aieverywhere.backend.models.Posts;

public class LikeSpecifications {
	public static Specification<Likes> hasLikeId(Long likeId) {
		return (root, query, criteriaBuilder) -> {
			if (likeId == null) {
				return criteriaBuilder.conjunction(); // 返回一个总是为真的 Predicate
			}
			return criteriaBuilder.equal(root.get("likeId"), likeId);
		};
	}
	
	public static Specification<Posts> hasPostId(Long postId) {
		return (root, query, criteriaBuilder) -> {
			if (postId == null) {
				return criteriaBuilder.conjunction(); // 返回一个总是为真的 Predicate
			}
			return criteriaBuilder.equal(root.get("postId"), postId);
		};
	}
}
