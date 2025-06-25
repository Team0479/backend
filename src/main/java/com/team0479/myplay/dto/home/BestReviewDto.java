package com.team0479.myplay.dto.home;

import java.time.LocalDateTime;

public class BestReviewDto {
    private Long reviewId;
    private String content;
    private Integer rating;
    private Integer likeCount;
    private Integer viewCount;
    private LocalDateTime createdAt;
    private Long performanceId;
    private String performanceTitle;
    private String performanceCategory;
    private Long userId;
    private String userNickname;
    private String userProfileImage;
    private String reviewImageUrl;

    public BestReviewDto() {}

    public BestReviewDto(Long reviewId, String content, Integer rating, Integer likeCount, Integer viewCount,
                        LocalDateTime createdAt, Long performanceId, String performanceTitle, String performanceCategory,
                        Long userId, String userNickname, String userProfileImage, String reviewImageUrl) {
        this.reviewId = reviewId;
        this.content = content;
        this.rating = rating;
        this.likeCount = likeCount;
        this.viewCount = viewCount;
        this.createdAt = createdAt;
        this.performanceId = performanceId;
        this.performanceTitle = performanceTitle;
        this.performanceCategory = performanceCategory;
        this.userId = userId;
        this.userNickname = userNickname;
        this.userProfileImage = userProfileImage;
        this.reviewImageUrl = reviewImageUrl;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
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

    public Long getPerformanceId() {
        return performanceId;
    }

    public void setPerformanceId(Long performanceId) {
        this.performanceId = performanceId;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getReviewImageUrl() {
        return reviewImageUrl;
    }

    public void setReviewImageUrl(String reviewImageUrl) {
        this.reviewImageUrl = reviewImageUrl;
    }
} 