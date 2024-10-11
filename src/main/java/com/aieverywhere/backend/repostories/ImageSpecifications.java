package com.aieverywhere.backend.repostories;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import com.aieverywhere.backend.models.Images;

public class ImageSpecifications {
	
	@Autowired ImageRepo imageRepo;

    public static Specification<Images> hasImageId(Long imgId) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.equal(root.get("imgId"), imgId);
    }
}
