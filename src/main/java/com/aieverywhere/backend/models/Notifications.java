package com.aieverywhere.backend.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Notifications {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long notificationId;
	private Long userId;
	private Long postId;
	private Long respondId;
	private Long senderId;
	private String contextType;
	@Enumerated(EnumType.STRING)
	private Type type;
	// true mean read false mean unread
	private boolean isRead = false;
	private LocalDateTime createdAt;

	public Notifications() {

	}

	public Notifications(Long userId, Long senderId, String contextType, Type type, LocalDateTime createdAt) {
		this.userId = userId;
		this.senderId = senderId;
		this.contextType = contextType;
		this.type = type;
		this.createdAt = createdAt;
	}

	public Long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getSenderId() {
		return senderId;
	}

	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}

	public String getContextType() {
		return contextType;
	}

	public void setContextType(String contextType) {
		this.contextType = contextType;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	public enum Type {
		Post, Response, Like, AddFriend
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public Long getRespondId() {
		return respondId;
	}

	public void setRespondId(Long respondId) {
		this.respondId = respondId;
	}

}
