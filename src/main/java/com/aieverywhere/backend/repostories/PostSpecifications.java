package com.aieverywhere.backend.repostories;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.aieverywhere.backend.models.Posts;
import com.aieverywhere.backend.models.Users;

import jakarta.persistence.criteria.Join;

public class PostSpecifications {
	public static Specification<Posts> hasPostId(Long postId) {
		return (root, query, criteriaBuilder) -> {
			if (postId == null) {
				return criteriaBuilder.conjunction(); // 返回一个总是为真的 Predicate
			}
			return criteriaBuilder.equal(root.get("postId"), postId);
		};
	}

	public static Specification<Posts> hasUserId(Long userId) {
		return (root, query, criteriaBuilder) -> {
			if (userId == null) {
				return criteriaBuilder.conjunction(); // 返回一个总是为真的 Predicate
			}
			return criteriaBuilder.equal(root.get("userId"), userId);
		};
	}

	public static Specification<Posts> greaterThanCreatedAt(LocalDateTime createdAt) {
		return (root, query, criteriaBuilder) -> {
			if (createdAt == null) {
				return criteriaBuilder.conjunction(); // 返回一个总是为真的 Predicate
			}
			return criteriaBuilder.greaterThan(root.get("createdAt"), createdAt);

		};
	}

	public static Specification<Posts> hasMoodTag(String moodTag) {
		return (root, query, criteriaBuilder) -> {
			if (moodTag == null) {
				return criteriaBuilder.conjunction(); // 返回一个总是为真的 Predicate
			}
			return criteriaBuilder.greaterThan(root.get("moodTag"), moodTag);

		};
	}
	
	public static Specification<Posts> userHasName(String username) {
	    return (root, query, criteriaBuilder) -> {
	        Join<Posts, Users> userJoin = root.join("user");
	        return criteriaBuilder.equal(userJoin.get("username"), username);
	    };
	}


	
}
