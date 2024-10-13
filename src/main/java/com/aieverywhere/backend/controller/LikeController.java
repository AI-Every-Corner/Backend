package com.aieverywhere.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aieverywhere.backend.services.LikesServices;

@RestController
@RequestMapping("/")
public class LikeController {

	@Autowired
	private LikesServices likesServices;
	
	@PutMapping("posts/{postId}/like")
	public ResponseEntity<?> addPostLike(@PathVariable Long postId, @RequestHeader Long userId) {
	    try {
	        likesServices.addPostLike(postId, userId);
	        return ResponseEntity.ok("Like added to post successfully");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(500).body("Failed to add like: " + e.getMessage());
	    }
	}

	
	@PutMapping("posts/{postId}/unlike")
	public ResponseEntity<?> removePostLike(@PathVariable Long postId, @RequestHeader Long userId) {
	    try {
	        likesServices.removePostLike(postId, userId);
	        return ResponseEntity.ok("Like removed to post successfully");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(500).body("Failed to remove like: " + e.getMessage());
	    }
	}

	@PutMapping("responses/{postId}/{responseId}/like")
	public ResponseEntity<?> addResponseLike(@PathVariable Long postId, @PathVariable Long responseId, @RequestHeader Long userId) {
	    try {
	        likesServices.addRespondLike(responseId, postId, userId);
	        return ResponseEntity.ok("Like added to response successfully");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(500).body("Failed to add like: " + e.getMessage());
	    }
	}
	
	@PutMapping("responses/{postId}/{responseId}/unlike")
	public ResponseEntity<?> removeResponseLike(@PathVariable Long postId, @PathVariable Long responseId, @RequestHeader Long userId) {
	    try {
	        likesServices.removeRespondLike(postId, responseId, userId);
	        return ResponseEntity.ok("Like removed to response successfully");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(500).body("Failed to remove like: " + e.getMessage());
	    }
	}
}
