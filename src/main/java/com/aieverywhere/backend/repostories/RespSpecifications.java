package com.aieverywhere.backend.repostories;

import org.springframework.data.jpa.domain.Specification;

import com.aieverywhere.backend.models.Responses;

public class RespSpecifications {

    public static Specification<Responses> hasPostId(Long postId) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("postId"), postId);
        };
    }
}
