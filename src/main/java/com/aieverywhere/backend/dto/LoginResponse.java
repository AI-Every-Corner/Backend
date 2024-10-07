package com.aieverywhere.backend.dto;

public class LoginResponse {
    private String token;
    private String imagePath;
    private Long userId;

    public LoginResponse(String token, String imagePath, Long userId) {
        this.token = token;
        this.imagePath = imagePath;
        this.userId=userId;
    }
    
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
