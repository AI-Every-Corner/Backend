package com.aieverywhere.backend.services;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

	public Users CreateUsers(Users user) {
		return userRepo.save(user);
		
	}

	public String login(String username, String password) {
		Users user = userRepo.findByUsername(username);
		if(user == null) {
			return "cant found user";
		}
		if(user.getPassword().equals(password)) {
			return "login success";
		}
		return "wrong password";
	}

	public String updateUser(Users user) {
		
		return "the result of update";

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
