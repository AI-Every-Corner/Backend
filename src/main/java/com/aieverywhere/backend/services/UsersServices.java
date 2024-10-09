package com.aieverywhere.backend.services;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aieverywhere.backend.models.Users;
import com.aieverywhere.backend.repostories.ImageSpecifications;
import com.aieverywhere.backend.repostories.UserRepo;
import com.aieverywhere.backend.repostories.UserSpecifications;

@Service
public class UsersServices implements UserDetailsService {
	private final UserRepo userRepo;
	private BCryptPasswordEncoder passwordEncoder1;
	
	@Autowired
    private ImageService imageService; // 注入圖片服務

	@Autowired
	public UsersServices(UserRepo userRepo) {
		this.userRepo = userRepo;
		this.passwordEncoder1 = new BCryptPasswordEncoder();

	}

	public Users createUsers(Users user) {
		// 加密密碼
		user.setPassword(passwordEncoder1.encode(user.getPassword()));
		return userRepo.save(user);
	}

	// public String login(String username, String password) {
	// Users user = userRepo.findByUsername(username);
	// if(user == null) {
	// return "cant found user";
	// }
	// if(user.getPassword().equals(password)) {
	// return "login success";
	// }
	// return "wrong password";
	// }

	public boolean existsByUsername(String username) {
		return userRepo.existsByUsername(username);
	}

	public boolean existsByEmail(String email) {
		return userRepo.existsByEmail(email);
	}

	public boolean checkPassword(String username, String password) {
		Users user = userRepo.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}
		return passwordEncoder1.matches(password, user.getPassword());
	}

	public Users findByUsername(String username) {
		return userRepo.findByUsername(username);
	}

	public Users findByUserId(Long userId) {
		return userRepo.findByUserId(userId);
	}

	public Users updateUser(Long userId, Users updatedUser, MultipartFile file) {
		System.out.println("in update");
		// 查詢現有的使用者
		Users existingUser = findByUserId(userId);
		if (existingUser == null) {
			throw new UsernameNotFoundException("User not found");
		}
		System.out.println("user exist");

		existingUser.setNickName(updatedUser.getNickName());
//		existingUser.setGender(updatedUser.getGender());
		existingUser.setBirth(updatedUser.getBirth());
		existingUser.setPhoneNum(updatedUser.getPhoneNum());
		existingUser.setEmail(updatedUser.getEmail());

		// 檢查是否有新密碼，如果有就進行加密並更新
		if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
			String encryptedPassword = passwordEncoder1.encode(updatedUser.getPassword()); // 加密新密碼
			existingUser.setPassword(encryptedPassword); // 更新加密後的密碼
		}
		
		 // 如果有上傳圖片，則更新圖片路徑
	    if (file != null && !file.isEmpty()) {
	        try {
	            String imagePath = imageService.uploadImage(file);
	            existingUser.setImagePath(imagePath); // 更新圖片路徑
	        } catch (Exception e) {
	            // 捕獲圖片上傳異常
	            throw new RuntimeException("圖片上傳失敗: " + e.getMessage());
	        }
	    }

		return userRepo.save(existingUser);
	}

	//public String updateUser(Users user) {

	//	return "the result of update";

	//}

	public String deleteUser(int userId) {
		return "the result of update";
	}

	// this is use for spring security
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user = userRepo.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole())));
	}

	public Users getUsersByUsersId(Long userId) throws Exception{
		try {
			return userRepo.findOne(UserSpecifications.hasUserId(userId)).get();
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Long getUsersCount() {
		return userRepo.count();
	}

}
