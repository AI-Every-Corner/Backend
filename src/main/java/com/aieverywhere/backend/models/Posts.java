package com.aieverywhere.backend.models;

import java.time.LocalDateTime;

import com.aieverywhere.backend.dto.PostResponseDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "posts")
public class Posts {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long postId;
	private Long imgId;
	private String content;
	private String moodTag;
	private Long moodScore;
	private Long likes;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "userId")  // Map to userId column in Users table
    private Users user;

	public Posts() {
		
	}

	public Posts(Long postId, Long userId, Long imgId, String content, String moodTag, Long moodScore, Long likes,
			LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.postId = postId;
		this.imgId = imgId;
		this.content = content;
		this.moodTag = moodTag;
		this.moodScore = moodScore;
		this.likes = likes;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public Long getImgId() {
		return imgId;
	}

	public void setImgId(Long imgId) {
		this.imgId = imgId;
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
	
	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}
	
	

}
