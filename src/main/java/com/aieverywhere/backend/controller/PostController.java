package com.aieverywhere.backend.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.query.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aieverywhere.backend.dto.PostResponseDTO;
import com.aieverywhere.backend.models.Posts;
import com.aieverywhere.backend.services.PostServices;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@RestController
@RequestMapping("/")
public class PostController {
	
	@Autowired
    private EntityManager entityManager;

	@GetMapping("posts")
    public Map<String, Object> queryPage(
    		@RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "10") int size
            ) {
        
        // SQL query
		String sqlStr = "SELECT p.post_id AS postId, p.content AS content, u.username AS username " +
                "FROM posts p JOIN users u ON p.user_id = u.user_id";
        
        // Pagination
        int offset = page * size;

        // Create query and set parameters
        Query query = entityManager.createNativeQuery(sqlStr);

        // Apply pagination
        query.setFirstResult(offset);
        query.setMaxResults(size);

     // Execute query and retrieve results
        List<Object[]> results = query.getResultList();

        // Transform results into PostResponseDTO
        List<PostResponseDTO> postRes = new ArrayList<>();
        for (Object[] row : results) {
            Long postId = ((Number) row[0]).longValue();
            String content = (String) row[1];
            String username = (String) row[2];
            postRes.add(new PostResponseDTO(postId, content, username));
        }
        
        // Modified count query to get the total number of items
        String cntSqlStr = "SELECT COUNT(*) FROM POSTS";
        Query cntQuery = entityManager.createNativeQuery(cntSqlStr);
        System.out.println("cnt: " + cntQuery);

        // Retrieve the total number of items
        Long totalItems = ((Number) cntQuery.getSingleResult()).longValue();
        int totalPages = (int) Math.ceil((double) totalItems / size);

        // Prepare response
        Map<String, Object> rs = new HashMap<>();
        rs.put("postRes", postRes);
        rs.put("currentPage", page);
        rs.put("totalItems", totalItems);
        rs.put("totalPages", totalPages);
        
	    return rs;
	}
	
}
