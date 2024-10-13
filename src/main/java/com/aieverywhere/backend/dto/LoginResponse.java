package com.aieverywhere.backend.dto;

public class LoginResponse {
    private String token;
    private String imagePath;
    private Long userId;
    private String coverPath;

    public LoginResponse(String token, String imagePath, Long userId, String coverPath) {
        this.token = token;
        this.imagePath = imagePath;
        this.userId=userId;
        this.coverPath = coverPath;
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

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }
}
