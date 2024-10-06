package com.aieverywhere.backend.services;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aieverywhere.backend.models.Users;
import com.aieverywhere.backend.repostories.UserRepo;

@Service
public class UsersServices implements UserDetailsService {
	private final UserRepo userRepo;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public UsersServices(UserRepo userRepo, PasswordEncoder passwordEncoder) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
	}

	public Users createUsers(Users user) {
		// 加密密碼
		user.setPassword(passwordEncoder.encode(user.getPassword()));
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
		return passwordEncoder.matches(password, user.getPassword());
	}

	public Users findByUsername(String username) {
		return userRepo.findByUsername(username);
	}

	public Users findByUserId(Long userId) {
		return userRepo.findByUserId(userId);
	}

	public Users updateUser(Long userId, Users updatedUser) {
		Users existingUser = findByUserId(userId);

		existingUser.setNickname(updatedUser.getNickname());
		existingUser.setGender(updatedUser.getGender());
		existingUser.setBirth(updatedUser.getBirth());
		existingUser.setPhoneNum(updatedUser.getPhoneNum());
		existingUser.setEmail(updatedUser.getEmail());

		// 檢查是否有新密碼，如果有就進行加密並更新
		if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
			String encryptedPassword = passwordEncoder.encode(updatedUser.getPassword()); // 加密新密碼
			existingUser.setPassword(encryptedPassword); // 更新加密後的密碼
		}

		return userRepo.save(existingUser);

	}

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

}
