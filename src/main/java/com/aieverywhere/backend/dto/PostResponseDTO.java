package com.aieverywhere.backend.dto;

public class PostResponseDTO {

	private Long postId;
	private String content;
	private String username; // The owner’s username
	private String imagePath;

	// Constructors
	public PostResponseDTO() {
	}

	public PostResponseDTO(Long postId, String content, String username, String imagePath) {
		this.postId = postId;
		this.content = content;
		this.username = username;
		this.imagePath = imagePath;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
}