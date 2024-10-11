package com.aieverywhere.backend.dto;

import java.time.LocalDateTime;

public class PostResponseDTO {

	private Long postId = 0L;
	private String content = null;
	private String nickname = null;
	private String imagePath = null;
	private LocalDateTime updateAt = null;
	private String location = null;
	private Long userId = 0L;
	private Long likes = 0L;

	// Constructors
	public PostResponseDTO() {
	}

	public PostResponseDTO(Long postId, String content, String nickname, String imagePath, LocalDateTime updateAt,
			String location, Long userId, Long likes) {
		super();
		this.postId = postId;
		this.content = content;
		this.nickname = nickname;
		this.imagePath = imagePath;
		this.updateAt = updateAt;
		this.location = location;
		this.userId = userId;
		this.likes = likes;
	}

	// Getters and Setters
	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
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

	public LocalDateTime getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(LocalDateTime updateAt) {
		this.updateAt = updateAt;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getLikes() {
		return likes;
	}

	public void setLikes(Long likes) {
		this.likes = likes;
	}
}