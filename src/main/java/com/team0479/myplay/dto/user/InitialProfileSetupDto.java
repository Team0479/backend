package com.team0479.myplay.dto.user;

public class InitialProfileSetupDto {
    private String nickname;
    private String profileImage;

    public InitialProfileSetupDto() {}

    public InitialProfileSetupDto(String nickname, String profileImage) {
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    // Getters and Setters
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
        return "InitialProfileSetupDto{" +
                "nickname='" + nickname + '\'' +
                ", profileImage='" + profileImage + '\'' +
                '}';
    }
} 