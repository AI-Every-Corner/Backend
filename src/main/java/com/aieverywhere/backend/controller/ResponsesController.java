package com.aieverywhere.backend.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aieverywhere.backend.services.ResponsesServices;

@RestController
@RequestMapping("/")
public class ResponsesController {
	
	private ResponsesServices respServ;

	public ResponsesController(ResponsesServices resServ) {
		this.respServ = resServ;
	}
	
	@GetMapping("responses")
	public ResponseEntity<?> queryPage(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		try {
			Map<String, Object> resMap = null;
			return ResponseEntity.status(200).body(resMap);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(e.getMessage());
		}
	}

}
