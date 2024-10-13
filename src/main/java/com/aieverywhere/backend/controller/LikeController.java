package com.aieverywhere.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

//	@PutMapping("responses/{responseId}/like")
//	public ResponseEntity<?> addResponseLike(@PathVariable Long responseId, @RequestHeader Long userId) {
//	    try {
//	        likesServices.addRespondLike(responseId, userId);
//	        return ResponseEntity.ok("Like added to response successfully");
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        return ResponseEntity.status(500).body("Failed to add like: " + e.getMessage());
//	    }
//	}

	
//	@GetMapping("removeLike")
//	public ResponseEntity<?> removeLike(@PathVariable Long postId,
//			@PathVariable Long responseId,
//			@PathVariable Long userId,
//			@PathVariable Long likeId) {
//		try {
//			if (postId != 0L) {
//	            likesServices.removePostLike(postId, userId, userId);
//	            return ResponseEntity.status(200).body("removal success");
//	        } else if (responseId != 0L) {
//	            likesServices.removeRespondLike(responseId, userId, userId);
//	            return ResponseEntity.status(200).body("removal success");
//	        } else {
//	            // Error case: neither postId nor responseId provided
//	            return ResponseEntity.status(400).body("Error: Either postId or responseId must be provided");
//	        }
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(500).body("Failed to add like: " + e.getMessage());
//		}
//	}
}
