package com.aieverywhere.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.query.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aieverywhere.backend.models.Posts;

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
        String sqlStr = "SELECT * FROM POSTS";
        
        // Pagination
        int offset = page * size;

        // Create query and set parameters
        Query query = entityManager.createNativeQuery(sqlStr, Posts.class);

        // Apply pagination
        query.setFirstResult(offset);
        query.setMaxResults(size);

        // Execute query
        List<Posts> posts = query.getResultList();
        
        // Modified count query to get the total number of items
        String cntSqlStr = "SELECT COUNT(*) FROM POSTS";
        Query cntQuery = entityManager.createNativeQuery(cntSqlStr);
        System.out.println("cnt: " + cntQuery);

        // Retrieve the total number of items
        Long totalItems = ((Number) cntQuery.getSingleResult()).longValue();
        int totalPages = (int) Math.ceil((double) totalItems / size);

        // Prepare response
        Map<String, Object> rs = new HashMap<>();
        rs.put("posts", posts);
        rs.put("currentPage", page);
        rs.put("totalItems", totalItems);
        rs.put("totalPages", totalPages);
        
	    return rs;
	}
}
