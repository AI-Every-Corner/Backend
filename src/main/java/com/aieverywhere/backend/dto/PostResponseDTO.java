package com.aieverywhere.backend.dto;

public class PostResponseDTO {

	private Long postId = 0L;
	private String content = null;
	private String nickname = null;
	private String imagePath = null;

	// Constructors
	public PostResponseDTO() {
	}

	public PostResponseDTO(Long postId, String content, String nickname, String imagePath) {
		this.postId = postId;
		this.content = content;
		this.nickname = nickname;
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
}