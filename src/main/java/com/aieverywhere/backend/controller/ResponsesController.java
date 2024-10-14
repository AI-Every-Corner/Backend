package com.aieverywhere.backend.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.aieverywhere.backend.models.Responses;
import com.aieverywhere.backend.services.ResponsesServices;

@RestController
@RequestMapping("/")
public class ResponsesController {
	
	private ResponsesServices respServ;

	public ResponsesController(ResponsesServices resServ) {
		this.respServ = resServ;
	}
	
	@GetMapping("responses/{postId}")
	public ResponseEntity<?> queryPage(@PathVariable Long postId,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		try {
			Map<String, Object> resMap = respServ.getPagedResponsesByPostId(postId, page, size);
			return ResponseEntity.status(200).body(resMap);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(e.getMessage());
		}
	}
	
	@PostMapping("createResponse")
	public ResponseEntity<?> createResponse(
			@RequestPart("response") Responses response) {
		try {
			respServ.createResponse(response);
			return ResponseEntity.status(201).body("Post created successfully");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("Failed to create post: " + e.getMessage());
		}
	}

}