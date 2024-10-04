package com.aieverywhere.backend.models;

import java.time.LocalDateTime;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;
	
	private String username;
	
	private String password;
	
	@Enumerated(EnumType.STRING)
	private Role role;
		
	private LocalDate birth;
	
	private int age;
	
	@Enumerated(EnumType.STRING)
	private Gender gender;
	
	private String email;
	
	private String phoneNum;
	
	private String imagePath;
	
	private String personality;
	
	private int emoLevel;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime updateAt;
	
	public Users() {
		
	}
	
	public Users(int userId, String username, String password, Role role, LocalDate birth, int age,
			Gender gender, String email, String phoneNum, String imagePath, String personality, int emoLevel,
			LocalDateTime createdAt, LocalDateTime updateAt) {
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.role = Role.User;
		this.birth = birth;
		this.age = age;
		this.gender = gender;
		this.email = email;
		this.phoneNum = phoneNum;
		this.imagePath = imagePath;
		this.personality = personality;
		this.emoLevel = emoLevel;
		this.createdAt = createdAt;
		this.updateAt = updateAt;
	}
	
	

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	

	public LocalDate getBirth() {
		return birth;
	}

	public void setBirth(LocalDate birth) {
		this.birth = birth;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getPersonality() {
		return personality;
	}

	public void setPersonality(String personality) {
		this.personality = personality;
	}

	public int getEmoLevel() {
		return emoLevel;
	}

	public void setEmoLevel(int emoLevel) {
		this.emoLevel = emoLevel;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(LocalDateTime updateAt) {
		this.updateAt = updateAt;
	}



	public enum Role {
		User, Ai, Admin
	}
	
	public enum Gender {
		Male, Female
	}
	
}
