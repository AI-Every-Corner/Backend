package com.aieverywhere.backend.controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.aieverywhere.backend.services.PostsServices;

@Controller
public class PostController {

	private final PostsServices postsServices;
	
	@Autowired
	public PostController(PostsServices postsServices) {
		this.postsServices = postsServices;
	}
	
	@PostMapping("/monthreview/{userId}")
	public ResponseEntity<?> monthReview(@PathVariable Long userId){
		try {
			Map<Integer,Double> month = postsServices.monthReviewData(userId);
			return ResponseEntity.status(200).body(month);

		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("failed "+e.getMessage());
		}
		
	}
	

	
}
