package com.aieverywhere.backend.repostories;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.aieverywhere.backend.models.Images;
import com.aieverywhere.backend.models.Posts;
import com.aieverywhere.backend.models.Users;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;

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

	public static Specification<Posts> hasImageId(Long imageId) {
		return (root, query, criteriaBuilder) -> {
			if (imageId == null) {
				return criteriaBuilder.conjunction(); // 返回一个总是为真的 Predicate
			}
			return criteriaBuilder.equal(root.get("imageId"), imageId);
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
	
    public static Specification<Posts> joinUsers() {
        return (root, query, criteriaBuilder) -> {
            // Join with Users table
            Join<Posts, Users> userJoin = root.join("user");
            return criteriaBuilder.conjunction(); // No filtering here, just creating the join
        };
    }

    public static Specification<Posts> joinImages() {
        return (root, query, criteriaBuilder) -> {
            // Join with Images table
            Join<Posts, Images> imageJoin = root.join("image");
            return criteriaBuilder.conjunction(); // No filtering here, just creating the join
        };
    }
    
    public static Specification<Object[]> fetchPostDetails() {
        return (root, query, criteriaBuilder) -> {
            // Join with Users table
//            Join<Posts, Users> userJoin = root.join("users");
            // Join with Images table
//            Join<Posts, Images> imageJoin = root.join("images");

            // Add the fields you want to select
            query.multiselect(
                root.get("postId"),       // Post ID
                root.get("content")      // Post content
//                userJoin.get("username"), // User's username
//                imageJoin.get("imagePath") // Image path
            );

            // No additional restrictions (WHERE clause), so return null
            return null;
        };
    }

    public static Specification<Posts> countPosts() {
        return (Root<Posts> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            // Modify the query to perform a count operation
            query.multiselect(criteriaBuilder.count(root));
            // Since we only want to count, no additional restrictions are needed, so return null
            return null;
        };
    }

	
}
