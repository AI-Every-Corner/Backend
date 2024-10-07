package com.aieverywhere.backend.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "responses")
public class Responses {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long responseId;
	private Long postId;
	private Long userId;
	private String content;
	private Long likes;
	private LocalDateTime createdAt;
	private LocalDateTime updateAt;

	public Responses() {

	}

	public Responses(Long responseId, Long postId, Long userId, String content, Long likes, Long supRepId,
			LocalDateTime createdAt,
			LocalDateTime updateAt) {
		this.responseId = responseId;
		this.postId = postId;
		this.userId = userId;
		this.content = content;
		this.likes = likes;
		this.createdAt = createdAt;
		this.updateAt = updateAt;
	}

	public Long getResponseId() {
		return responseId;
	}

	public void setResponseId(Long responseId) {
		this.responseId = responseId;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public Long getLikes() {
		return likes;
	}

	public void setLikes(Long likes) {
		this.likes = likes;
	}

}
