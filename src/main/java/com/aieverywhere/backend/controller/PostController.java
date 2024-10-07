package com.aieverywhere.backend.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aieverywhere.backend.dto.PostResponseDTO;
import com.aieverywhere.backend.services.PostServices;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@RestController
@RequestMapping("/")
public class PostController {

	private PostServices postServices;
	private EntityManager entityManager;

	@Autowired
	public PostController(PostServices postServices, EntityManager entityManager) {
		this.postServices = postServices;
		this.entityManager = entityManager;
	}
	
	@GetMapping("")
	public ResponseEntity<?> test() {
		return ResponseEntity.status(200).body("hello");
	}

	@GetMapping("posts")
	public ResponseEntity<?> queryPage(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		System.out.println("in posts");

		try {
			// SQL query
//			String sqlStr = "SELECT p.post_id AS postId, p.content AS content, u.username AS username, i.image_path AS imagePath "
//					+ "FROM posts p JOIN users u ON p.user_id = u.user_id " + "JOIN images i ON p.img_id = i.image_id";
//
//			// Pagination
//			int offset = page * size;
//
//			// Create query and set parameters
//			Query query = entityManager.createNativeQuery(sqlStr);
//
//			// Apply pagination
//			query.setFirstResult(offset);
//			query.setMaxResults(size);
//
//			// Execute query and retrieve results
//			List<Object[]> results = query.getResultList();
//
//			// Transform results into PostResponseDTO
//			List<PostResponseDTO> postRes = new ArrayList<>();
//			for (Object[] row : results) {
//				Long postId = ((Number) row[0]).longValue();
//				String content = (String) row[1];
//				String username = (String) row[2];
//				String imagePath = (String) row[3];
//				postRes.add(new PostResponseDTO(postId, content, username, imagePath));
//			}
//
//			// Modified count query to get the total number of items
//			String cntSqlStr = "SELECT COUNT(*) FROM POSTS";
//			Query cntQuery = entityManager.createNativeQuery(cntSqlStr);
//			System.out.println("cnt: " + cntQuery);
//			
//			// Retrieve the total number of items
//			Long totalItems = ((Number) cntQuery.getSingleResult()).longValue();
//			int totalPages = (int) Math.ceil((double) totalItems / size);
//
//			// Prepare response
//			Map<String, Object> postMap = new HashMap<>();
//			postMap.put("postRes", postRes);
//			postMap.put("currentPage", page);
//			postMap.put("totalItems", totalItems);
//			postMap.put("totalPages", totalPages);

			// Prepare response
			Map<String, Object> postMap = postServices.getAllPosts(page, size);
			return ResponseEntity.status(200).body(postMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.status(500).body("failed: " + e.getMessage());
		}
	}

	@PostMapping("yearReview/{userId}")
	public ResponseEntity<?> monthReview(@PathVariable Long userId) {
		try {
			Map<LocalDateTime, Double> month = postServices.monthReviewData(userId);
			return ResponseEntity.status(200).body(month);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("failed: " + e.getMessage());
		}

	}
}
