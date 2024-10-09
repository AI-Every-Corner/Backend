package com.aieverywhere.backend.dto;

import java.time.LocalDateTime;

public class RespResponseDTO {

	private Long responseId;
	private Long postId;
	private Long userId;
	private String content;
	private String nickname = null;
	private String imagePath = null;
	private Long likes;
	private LocalDateTime createdAt;
	private LocalDateTime updateAt;
	
	public RespResponseDTO() {
	}

	public RespResponseDTO(Long responseId, Long postId, Long userId, String content, String nickname, String imagePath,
			Long likes, LocalDateTime createdAt, LocalDateTime updateAt) {
		this.responseId = responseId;
		this.postId = postId;
		this.userId = userId;
		this.content = content;
		this.nickname = nickname;
		this.imagePath = imagePath;
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

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
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

	public LocalDateTime getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(LocalDateTime updateAt) {
		this.updateAt = updateAt;
	}
}
