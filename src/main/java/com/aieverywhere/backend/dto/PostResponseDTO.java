package com.aieverywhere.backend.dto;

public class PostResponseDTO {

    private Long postId;
    private String content;
    private String username;  // The owner’s username

    // Constructors
    public PostResponseDTO() {
    }
    
    public PostResponseDTO(Long postId, String content, String username) {
        this.postId = postId;
        this.content = content;
        this.username = username;
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
}
