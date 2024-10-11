package com.aieverywhere.backend.dto;

import com.aieverywhere.backend.models.Notifications;

public class NotificationRequest {
	private Long userId;
	private String contextType;
	private Notifications.Type type;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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
}