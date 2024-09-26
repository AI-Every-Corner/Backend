package com.aieverywhere.backend.models;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "posts")
public class PostsModel {

	  @Id
	  @GeneratedValue(strategy=GenerationType.AUTO)
	  private int post_id;
	  @Column(name="user_id")
	  private int user_id;
	  @Column(name="img_id")
	  private int img_id;
	  @Column(name="post_title")
	  private String post_title;
	  @Column(name="content")
	  private String content;
	  @Column(name="mood_tags")
	  private String mood_tags;
	  @Column(name="mood_score")
	  private int mood_score;
	  @Column(name="likes")
	  private int likes;
	  @Column(name="created_at")
	  private Date created_at;
	  @Column(name="updated_at")
	  private Date updated_at;
	  
	public PostsModel(int post_id, int user_id, int img_id, String post_title, String content, String mood_tags,
			int mood_score, int likes, Date created_at, Date updated_at) {
		super();
		this.post_id = post_id;
		this.user_id = user_id;
		this.img_id = img_id;
		this.post_title = post_title;
		this.content = content;
		this.mood_tags = mood_tags;
		this.mood_score = mood_score;
		this.likes = likes;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}

	public int getPost_id() {
		return post_id;
	}

	public void setPost_id(int post_id) {
		this.post_id = post_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getImg_id() {
		return img_id;
	}

	public void setImg_id(int img_id) {
		this.img_id = img_id;
	}

	public String getPost_title() {
		return post_title;
	}

	public void setPost_title(String post_title) {
		this.post_title = post_title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMood_tags() {
		return mood_tags;
	}

	public void setMood_tags(String mood_tags) {
		this.mood_tags = mood_tags;
	}

	public int getMood_score() {
		return mood_score;
	}

	public void setMood_score(int mood_score) {
		this.mood_score = mood_score;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}
}
