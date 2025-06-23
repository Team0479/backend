package com.team0479.myplay.dto.home;

public class BestPlayerDto {
    private Long userId;
    private String nickname;
    private String profileImage;
    private Integer calendarCount;
    private Integer reviewCount;
    private Integer totalActivity;

    public BestPlayerDto() {}

    public BestPlayerDto(Long userId, String nickname, String profileImage, 
                        Integer calendarCount, Integer reviewCount, Integer totalActivity) {
        this.userId = userId;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.calendarCount = calendarCount;
        this.reviewCount = reviewCount;
        this.totalActivity = totalActivity;
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

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public Integer getCalendarCount() {
        return calendarCount;
    }

    public void setCalendarCount(Integer calendarCount) {
        this.calendarCount = calendarCount;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public Integer getTotalActivity() {
        return totalActivity;
    }

    public void setTotalActivity(Integer totalActivity) {
        this.totalActivity = totalActivity;
    }
} 