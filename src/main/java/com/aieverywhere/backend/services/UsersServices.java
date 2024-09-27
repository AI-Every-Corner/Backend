package com.aieverywhere.backend.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aieverywhere.backend.models.UsersModel;
import com.aieverywhere.backend.repostories.UsersRepostory;

@Service
public class UsersServices {

	@Autowired
	private UsersRepostory usersRepostory;
	
	public UsersModel findAllByID(String userID){
		return null;
	}
	public UsersModel findAllByEmailOrPhone(String email, String phone){
		return null;
	}
	public List<UsersModel> findAllByName(String name){
		return null;
	}
	public List<UsersModel> findAllByRole(String role){
		return null;
	}
	public List<UsersModel> findByPersonality(String personality){
		return null;
	}
	public List<UsersModel> findByEmo_level(int emoLevel){
		return null;
	}
	public List<UsersModel> findByCreated_atBetween(Date start, Date end){
		return null;
	}
	public List<UsersModel> findByUpdate_atBetween(Date start, Date end){
		return null;
	}
}
