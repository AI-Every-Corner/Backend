package com.aieverywhere.backend.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
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
	private Long userId;
	
	private String username;
	
	private String nickName;

	private String password;

	@Enumerated(EnumType.STRING)
	private Role role;

	private LocalDate birth;
	
	private Long age;
	
	@Enumerated(EnumType.STRING)
	private Gender gender;

	private String email;

	private String phoneNum;
	@Column(nullable = true)
	private String imagePath;

	@Column(nullable = true)
	private String coverPath;

	@Column(nullable = true)
	private String personality;

	
	private Long emoLevel;
	
	private LocalDateTime createdAt;

	private LocalDateTime updateAt;

	public Users() {

	}

	public Users(Users argUser) {
		this.userId = argUser.getUserId();
		this.username = argUser.getUsername();
		this.password = argUser.getPassword();
		this.role = Role.User;
		this.nickName = argUser.getNickName();
		this.birth = argUser.getBirth();
		this.age = argUser.getAge();
		this.gender = argUser.getGender();
		this.email = argUser.getEmail();
		this.phoneNum = argUser.getPhoneNum();
		this.imagePath = argUser.getImagePath();
		this.coverPath = argUser.getCoverPath();
		this.personality = argUser.getPersonality();
		this.emoLevel = argUser.getEmoLevel();
		this.createdAt = argUser.getCreatedAt();
		this.updateAt = argUser.getUpdateAt();
	}

	public Users(Long userId, String username, String password, Role role, String nickName, LocalDate birth,
			Long age, Gender gender,
			String email, String phoneNum, String imagePath, String coverPath, String personality, Long emoLevel, LocalDateTime createdAt,
			LocalDateTime updateAt) {

		this.userId = userId;
		this.username = username;
		this.nickName = nickName;
		this.password = password;
		this.role = Role.User;
		this.birth = birth;
		this.age = age;
		this.gender = gender;
		this.email = email;
		this.phoneNum = phoneNum;
		this.imagePath = imagePath;
		this.coverPath = coverPath;
		this.personality = personality;
		this.emoLevel = emoLevel;
		this.createdAt = createdAt;
		this.updateAt = updateAt;
	}

	public Long getUserId() {

		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickname) {
		this.nickName = nickname;
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

	public Long getAge() {
		return age;
	}

	public void setAge(Long age) {
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

	public String getCoverPath() {
		return coverPath;
	}

	public void setCoverPath(String coverPath) {
		this.coverPath = coverPath;
	}

	public String getPersonality() {
		return personality;
	}

	public void setPersonality(String personality) {
		this.personality = personality;
	}

	public Long getEmoLevel() {

		return emoLevel;
	}

	public void setEmoLevel(Long emoLevel) {
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
