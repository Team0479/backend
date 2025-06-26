package com.team0479.myplay.dto.user;

public class UserProfileDto {
    private Long userId;
    private String nickname;
    private String profileImageUrl;
    private Integer level;
    private Integer exp;
    private Integer requiredExpForNextLevel;

    public UserProfileDto() {}

    public UserProfileDto(Long userId, String nickname, String profileImageUrl, Integer level, Integer exp, Integer requiredExpForNextLevel) {
        this.userId = userId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.level = level;
        this.exp = exp;
        this.requiredExpForNextLevel = requiredExpForNextLevel;
    }

    // Getters and Setters
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

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getExp() {
        return exp;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public Integer getRequiredExpForNextLevel() {
        return requiredExpForNextLevel;
    }

    public void setRequiredExpForNextLevel(Integer requiredExpForNextLevel) {
        this.requiredExpForNextLevel = requiredExpForNextLevel;
    }
} 