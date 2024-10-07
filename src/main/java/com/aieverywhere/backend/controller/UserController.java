package com.aieverywhere.backend.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aieverywhere.backend.config.JwtUtils;
import com.aieverywhere.backend.dto.LoginResponse;
import com.aieverywhere.backend.models.Users;
import com.aieverywhere.backend.repostories.LoginRequest;
import com.aieverywhere.backend.services.ImageService;
import com.aieverywhere.backend.services.UsersServices;

@RestController
@RequestMapping("/api/auth")
public class UserController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private UsersServices usersServices;

	@Autowired
	private ImageService imageService;

	// signup
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestParam("image") MultipartFile file,
			@RequestParam("username") String username, @RequestParam("password") String password,
			@RequestParam("birth") String birth, @RequestParam("gender") String gender,
			@RequestParam("email") String email, @RequestParam("phoneNum") String phoneNum,
			@RequestParam(value = "personality", required = false) String personality,
			@RequestParam(value = "emoLevel", required = false) Integer emoLevel) {

		try {
			// 檢查用戶名是否已存在
			if (usersServices.existsByUsername(username)) {
				return ResponseEntity.badRequest().body("錯誤: 用戶名已被使用!");
			}

			// 檢查郵箱是否已存在
			if (usersServices.existsByEmail(email)) {
				return ResponseEntity.badRequest().body("錯誤: 郵箱已被使用!");
			}

			// 上傳圖片
			String imagePath = imageService.uploadImage(file);

			// 創建新用戶
			Users newUser = new Users();
			newUser.setUsername(username);
			newUser.setPassword(password);
			newUser.setRole(Users.Role.User);
			LocalDate birthDate = LocalDate.parse(birth.toString());
			newUser.setBirth(birthDate);
			newUser.setGender(Users.Gender.valueOf(gender));
			newUser.setEmail(email);
			newUser.setPhoneNum(phoneNum);
			newUser.setImagePath(imagePath);
			newUser.setCreatedAt(LocalDateTime.now());
			newUser.setUpdateAt(LocalDateTime.now());

			// 計算年齡
			Long age = (long) Period.between(birthDate, LocalDate.now()).getYears();
			newUser.setAge(age);

			// 保存用戶
			Users savedUser = usersServices.createUsers(newUser);

			if (savedUser != null) {
				return ResponseEntity.ok("用戶註冊成功!");
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("用戶註冊失敗");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("註冊過程中發生錯誤: " + e.getMessage());
		}
	}

	// login
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
		try {

			// 先檢查用戶是否存在
			UserDetails userDetails = usersServices.loadUserByUsername(loginRequest.getUsername());

			// 驗證密碼
			if (!usersServices.checkPassword(loginRequest.getUsername(), loginRequest.getPassword())) {
				System.out.println("密碼錯誤：用戶 " + loginRequest.getUsername());
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("認證失敗：密碼錯誤");
			}

			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
			
			SecurityContextHolder.getContext().setAuthentication(authentication);

			String jwt = jwtUtils.generateToken(userDetails);

			Users user = usersServices.findByUsername(loginRequest.getUsername());
			LoginResponse response = new LoginResponse(jwt, user.getImagePath(),user.getUserId());

			System.out.println("用戶 " + loginRequest.getUsername() + " 登錄成功");
			return ResponseEntity.status(200).body(response);

		} catch (AuthenticationException e) {
			System.out.println("認證失敗：" + e.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("認證失敗：用戶名或密碼錯誤");
		} catch (Exception e) {
			System.out.println("登錄過程中發生錯誤：" + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("登錄過程中發生錯誤");
		}
	}

}
