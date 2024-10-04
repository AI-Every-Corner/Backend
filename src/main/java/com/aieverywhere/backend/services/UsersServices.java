package com.aieverywhere.backend.services;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.aieverywhere.backend.models.Users;
import com.aieverywhere.backend.repostories.UserRepo;

@Service
public class UsersServices implements UserDetailsService {
	private final UserRepo userRepo;

	@Autowired
	public UsersServices(UserRepo userRepo) {
		this.userRepo = userRepo;
	}
	
	public Long getUsersCount() {
		return userRepo.count();
	}

	public String createUsers(Users user) throws Exception {
		try {
			String hashPassword = user.getPassword();
			hashPassword = new BCryptPasswordEncoder().encode(hashPassword);
			user.setPassword(hashPassword);
			userRepo.save(user);
			return "register success";
		} catch (Exception e) {
			e.printStackTrace();
			return "register failed " + e.getMessage();
		}
	}

	public Users getUsersByUsersId(Long userId) {
		try {
			Users getUser = userRepo.findByUserId(userId);
			return getUser;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String deleteUser(Long userId) throws Exception {
		try {
			userRepo.deleteById(userId);
			return "delete success";
		} catch (Exception e) {
			e.printStackTrace();
			return "delete failed";
		}
	}

	public String updateUser(Long userId ,Users user) throws Exception {
		try {
			user.setUserId(userId);
			userRepo.save(user);
			return "update success";
		} catch (Exception e) {
			e.printStackTrace();
			return "update failed";
		}
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
