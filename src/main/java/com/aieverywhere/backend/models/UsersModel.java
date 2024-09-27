package com.aieverywhere.backend.models;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UsersModel {

	  @Id
	  @GeneratedValue(strategy=GenerationType.AUTO)
	  private int user_id;
	  @Column(name="role", nullable = false)
	  private String role;
	  @Column(name="name", nullable = false)
	  private String name;
	  @Column(name="birth")
	  private Date birth;
	  @Column(name="age")
	  private int age;
	  @Column(name="gender")
	  private String gender;
	  @Column(name="email", nullable = false, unique = true)
	  private String email;
	  @Column(name="password", nullable = false)
	  private String password;
	  @Column(name="phone", nullable = false, unique = true)
	  private String phone;
	  @Column(name="image_path")
	  private String image_path;
	  @Column(name="personality")
	  private String personality;
	  @Column(name="emo_level")
	  private int emo_level;
	  @Column(name="created_at")
	  private Date created_at;
	  @Column(name="update_at")
	  private Date update_at;

	public UsersModel(int user_id, String role, String name, Date birth, int age, String gender, String email,
			String password, String phone, String image_path, String personality, int emo_level, Date created_at,
			Date update_at) {
		super();
		this.user_id = user_id;
		this.role = role;
		this.name = name;
		this.birth = birth;
		this.age = age;
		this.gender = gender;
		this.email = email;
		this.password = password;
		this.phone = phone;
		this.image_path = image_path;
		this.personality = personality;
		this.emo_level = emo_level;
		this.created_at = created_at;
		this.update_at = update_at;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getImage_path() {
		return image_path;
	}

	public void setImage_path(String image_path) {
		this.image_path = image_path;
	}

	public String getPersonality() {
		return personality;
	}

	public void setPersonality(String personality) {
		this.personality = personality;
	}

	public int getEmo_level() {
		return emo_level;
	}

	public void setEmo_level(int emo_level) {
		this.emo_level = emo_level;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getUpdate_at() {
		return update_at;
	}

	public void setUpdate_at(Date update_at) {
		this.update_at = update_at;
	}
}
