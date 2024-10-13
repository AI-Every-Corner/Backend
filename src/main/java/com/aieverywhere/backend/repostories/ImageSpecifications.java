package com.aieverywhere.backend.repostories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import com.aieverywhere.backend.models.Images;

public class ImageSpecifications {

    @Autowired
    ImageRepo imageRepo;

    public static Specification<Images> hasImageId(Long imgId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("imgId"), imgId);
    }
}
