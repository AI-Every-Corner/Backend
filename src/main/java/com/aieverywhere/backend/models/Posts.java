package com.aieverywhere.backend.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Table;

@Entity
@Table(name = "posts")
public class Posts {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long postId;
	private Long userId;
	private Long imageId;
	private String content;
	private String moodTag;
	private Long moodScore;
	private Long likes;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String location;

	public Posts() {

	}

	public Posts(Long postId, Long userId, Long imageId, String content, String moodTag, Long moodScore, Long likes,
			LocalDateTime createdAt, LocalDateTime updatedAt, String location) {
		this.postId = postId;
		this.userId = userId;
		this.imageId = imageId;
		this.content = content;
		this.moodTag = moodTag;
		this.moodScore = moodScore;
		this.likes = likes;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.location = location;
	}

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getImageId() {
		return imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMoodTag() {
		return moodTag;
	}

	public void setMoodTag(String moodTag) {
		this.moodTag = moodTag;
	}

	public Long getMoodScore() {
		return moodScore;
	}

	public void setMoodScore(Long moodScore) {
		this.moodScore = moodScore;
	}

	public Long getLikes() {
		return likes;
	}

	public void setLikes(Long likes) {
		this.likes = likes;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}