package com.aieverywhere.backend.repostories;

import org.springframework.data.jpa.domain.Specification;

import com.aieverywhere.backend.models.Users;

public class UserSpecifications {
	public static Specification<Users> hasUserId(Long userId) {
		return (root, query, criteriaBuilder) -> {
			if (userId == null) {
				return criteriaBuilder.conjunction();
			}
			return criteriaBuilder.equal(root.get("userId"), userId);
		};
	}
}
