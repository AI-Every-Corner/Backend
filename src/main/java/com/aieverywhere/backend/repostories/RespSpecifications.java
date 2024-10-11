package com.aieverywhere.backend.repostories;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.aieverywhere.backend.models.Posts;
import com.aieverywhere.backend.models.Responses;

import jakarta.persistence.criteria.Join;

public class RespSpecifications {

    public static Specification<Responses> hasPostId(Long postId) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("postId"), postId);
        };
    }
}
