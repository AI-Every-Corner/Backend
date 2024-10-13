package com.aieverywhere.backend.dto;

import java.time.LocalDateTime;
import com.aieverywhere.backend.models.Notifications;

public class NotificationRequest {
	private Long userId;
	private Long senderId;
	private String contextType;
	private Notifications.Type type;
	private LocalDateTime createdAt;

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

	public Notifications.Type getType() {
		return type;
	}

	public void setType(Notifications.Type type) {
		this.type = type;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}