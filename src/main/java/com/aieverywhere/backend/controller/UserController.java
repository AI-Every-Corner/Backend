package com.aieverywhere.backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.aieverywhere.backend.config.JwtUtils;
import com.aieverywhere.backend.models.Users;
import com.aieverywhere.backend.services.UsersServices;

@Controller
public class UserController {
	private final AuthenticationManager authenticationManager;
	private final JwtUtils jwtUtils;
	private final UsersServices usersServices;

	@Autowired
	public UserController(UsersServices usersServices, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
		this.authenticationManager = authenticationManager;
		this.jwtUtils = jwtUtils;
		this.usersServices = usersServices;

	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody Users registerRequest) {
		try {
			return ResponseEntity.status(200).body(usersServices.createUsers(registerRequest));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("register failed " + e.getMessage());
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Users loginRequest) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			UserDetails user = usersServices.loadUserByUsername(loginRequest.getUsername());

			String jwt = jwtUtils.generateToken(user);
			Map<String, String> response = new HashMap<>();
			response.put("jwttoken", jwt);
			response.put("message", "login success!");
			System.out.println(jwt);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(400).body("login failed " + e.getMessage());
		}
	}

	@DeleteMapping("/delete/{userId}")
	public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
		try {
			return ResponseEntity.status(200).body(usersServices.deleteUser(userId));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("delete failed " + e.getMessage());
		}
	}

	@PutMapping("/update/{userId}")
	public ResponseEntity<?> updateUser(@PathVariable Long userId ,@RequestBody Users user) {
		try {
			return ResponseEntity.status(200).body(usersServices.updateUser(userId,user));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("update failed " + e.getMessage());
		}
	}

}
