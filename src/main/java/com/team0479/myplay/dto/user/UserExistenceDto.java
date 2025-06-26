package com.team0479.myplay.dto.user;

public class UserExistenceDto {
    private Long userId;
    private boolean exists;
    private boolean profileCompleted;  // 닉네임과 프로필 이미지가 설정되었는지
    private String nickname;
    private String profileImage;

    public UserExistenceDto() {}

    public UserExistenceDto(Long userId, boolean exists, boolean profileCompleted, String nickname, String profileImage) {
        this.userId = userId;
        this.exists = exists;
        this.profileCompleted = profileCompleted;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public boolean isProfileCompleted() {
        return profileCompleted;
    }

    public void setProfileCompleted(boolean profileCompleted) {
        this.profileCompleted = profileCompleted;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    @Override
    public String toString() {
        return "UserExistenceDto{" +
                "userId=" + userId +
                ", exists=" + exists +
                ", profileCompleted=" + profileCompleted +
                ", nickname='" + nickname + '\'' +
                ", profileImage='" + profileImage + '\'' +
                '}';
    }
} 