package com.team0479.myplay.dto.plaza;

import java.time.LocalDateTime;

public class ReviewListDto {
    private Long id;
    private String content;
    private Integer rating;
    private Integer likeCount;
    private Integer viewCount;
    private LocalDateTime createdAt;
    private String performanceTitle;
    private String performanceCategory;
    private String performanceVenue;
    private String performanceImageUrl;
    private Long performanceId;
    private String userNickname;
    private String userProfileImage;
    private Long userId;

    public ReviewListDto() {}

    public ReviewListDto(Long id, String content, Integer rating, Integer likeCount, 
                        Integer viewCount, LocalDateTime createdAt, String performanceTitle,
                        String performanceCategory, String performanceVenue, String performanceImageUrl,
                        Long performanceId, String userNickname, String userProfileImage, Long userId) {
        this.id = id;
        this.content = content;
        this.rating = rating;
        this.likeCount = likeCount;
        this.viewCount = viewCount;
        this.createdAt = createdAt;
        this.performanceTitle = performanceTitle;
        this.performanceCategory = performanceCategory;
        this.performanceVenue = performanceVenue;
        this.performanceImageUrl = performanceImageUrl;
        this.performanceId = performanceId;
        this.userNickname = userNickname;
        this.userProfileImage = userProfileImage;
        this.userId = userId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getPerformanceTitle() {
        return performanceTitle;
    }

    public void setPerformanceTitle(String performanceTitle) {
        this.performanceTitle = performanceTitle;
    }

    public String getPerformanceCategory() {
        return performanceCategory;
    }

    public void setPerformanceCategory(String performanceCategory) {
        this.performanceCategory = performanceCategory;
    }

    public String getPerformanceVenue() {
        return performanceVenue;
    }

    public void setPerformanceVenue(String performanceVenue) {
        this.performanceVenue = performanceVenue;
    }

    public String getPerformanceImageUrl() {
        return performanceImageUrl;
    }

    public void setPerformanceImageUrl(String performanceImageUrl) {
        this.performanceImageUrl = performanceImageUrl;
    }

    public Long getPerformanceId() {
        return performanceId;
    }

    public void setPerformanceId(Long performanceId) {
        this.performanceId = performanceId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public void setUserProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
} 