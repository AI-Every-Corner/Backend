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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aieverywhere.backend.config.JwtUtils;
import com.aieverywhere.backend.dto.LoginResponse;
import com.aieverywhere.backend.models.Users;
import com.aieverywhere.backend.models.Users.Gender;
import com.aieverywhere.backend.repostories.LoginRequest;
import com.aieverywhere.backend.services.ImagesServices;
import com.aieverywhere.backend.services.UsersServices;

@CrossOrigin(origins = "http://localhost:3000")
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
	private ImagesServices imagesServices;

	// signup
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestParam("image") MultipartFile file,
			@RequestParam("cover") MultipartFile coverFile,
			@RequestParam("username") String username, @RequestParam("nickName") String nickName,
			@RequestParam("password") String password,
			@RequestParam("birth") String birth, @RequestParam("gender") Gender gender,
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
			String imagePath = imagesServices.uploadImage(file);
			String coverPath = imagesServices.uploadImage(coverFile);
			// 創建新用戶
			Users newUser = new Users();
			newUser.setUsername(username);
			newUser.setNickName(nickName);
			newUser.setPassword(password);
			newUser.setRole(Users.Role.User);
			LocalDate birthDate = LocalDate.parse(birth.toString());
			newUser.setBirth(birthDate);
			newUser.setGender(gender);
			newUser.setEmail(email);
			newUser.setPhoneNum(phoneNum);
			newUser.setImagePath(imagePath);
			newUser.setCoverPath(coverPath);
			newUser.setCreatedAt(LocalDateTime.now());
			newUser.setUpdateAt(LocalDateTime.now());

			// 計算年齡
			long age = Period.between(birthDate, LocalDate.now()).getYears();
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
			LoginResponse response = new LoginResponse(jwt, user.getImagePath(), user.getUserId(), user.getCoverPath());

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

	@GetMapping("/{userId}")
	public ResponseEntity<Users> getUser(@PathVariable Long userId) {
		try {
			Users user = usersServices.findByUserId(userId);
			if (user != null) {
				return ResponseEntity.ok(user);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 找不到用戶
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 錯誤處理
		}
	}

	@PutMapping("/{userId}")
	public ResponseEntity<?> updateUser(@PathVariable Long userId,
			@RequestParam("nickName") String nickName,
			@RequestParam("gender") Gender gender,
			@RequestParam("birth") LocalDate birth,
			@RequestParam("phoneNum") String phoneNum,
			@RequestParam("email") String email,
			@RequestParam(value = "password", required = false) String password,
			@RequestParam(value = "image", required = false) MultipartFile file,
			@RequestParam(value = "cover", required = false) MultipartFile coverFile) {

		try {
			// 組裝更新用戶的資料
			Users updatedUser = new Users();
			updatedUser.setNickName(nickName);
			updatedUser.setGender(gender);
			updatedUser.setBirth(birth);
			updatedUser.setPhoneNum(phoneNum);
			updatedUser.setEmail(email);

			if (password != null && !password.isEmpty()) {
				updatedUser.setPassword(password); // 如果有新密碼，則設置
			}

			// 調用服務層來更新用戶資料
			Users user = usersServices.updateUser(userId, updatedUser, file, coverFile);
			return ResponseEntity.ok(user); // 更新成功，返回更新後的用戶資料
		} catch (RuntimeException e) {
			// 捕捉用戶不存在或其他問題時的異常
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("用戶不存在或更新失敗");
		} catch (Exception e) {
			// 捕捉任何其他的異常
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("發生錯誤，更新失敗");
		}

	}

	@GetMapping("/{userId}/email")
	public ResponseEntity<String> getEmail(@PathVariable Long userId) {
		try {
			Users user = usersServices.findByUserId(userId);
			if (user != null) {
				return ResponseEntity.ok(user.getEmail());
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("用戶不存在");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("發生錯誤，獲取郵箱失敗");
		}
	}
}
