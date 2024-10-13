package com.aieverywhere.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aieverywhere.backend.services.GeminiService;

@Controller
@RequestMapping("/api/gemini")
public class AiController {
	private GeminiService geminiService;

	@Autowired
	public AiController(GeminiService geminiService) {
		this.geminiService = geminiService;
	}

	@PostMapping("/AiRespondPost/{postId}")
	public ResponseEntity<?> AiRespondPost(@PathVariable Long postId) {
		try {
			String respond = geminiService.AiRespondPost(postId);
			return ResponseEntity.status(200).body(respond);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("failed " + e.getMessage());
		}

	}

	@PostMapping("/AiRespondToRespond/{postId}")
	public ResponseEntity<?> AiRespondToRespond(@PathVariable Long postId) {
		try {
			String respond = geminiService.AiRespondToRespond(postId);
			return ResponseEntity.status(200).body(respond);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("failed " + e.getMessage());
		}

	}

	@GetMapping("/AiPost")
	public ResponseEntity<?> aiPost() {
		try {
			String respond = geminiService.aiCreatePost();
			return ResponseEntity.status(200).body(respond);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("failed " + e.getMessage());
		}
	}

}
