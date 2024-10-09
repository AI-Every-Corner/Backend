package com.aieverywhere.backend.dto;

public class FriendsDTO {
    private Long userId;
    private String nickname;
    private String imagePath;
    private Long friendId;

    public  FriendsDTO(){
    }

    public FriendsDTO(Long userId, String nickname, String imagePath, Long friendId) {
        this.userId = userId;
        this.nickname = nickname;
        this.imagePath = imagePath;
        this.friendId = friendId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
    
    public Long getFriendId() {
        return friendId;
    }

    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }

    

    
    


}
