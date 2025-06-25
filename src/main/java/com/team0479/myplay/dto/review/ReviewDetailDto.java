package com.team0479.myplay.dto.review;

import java.time.LocalDateTime;
import java.util.List;

public class ReviewDetailDto {
    private Long reviewId;
    private String title;
    private String content;
    private Integer rating;
    private Integer likeCount;
    private Integer viewCount;
    private Integer commentCount;
    private LocalDateTime createdAt;
    
    // 공연 정보
    private Long performanceId;
    private String performanceTitle;
    private String performanceCategory;
    private String performanceVenue;
    private String performanceImageUrl;
    
    // 작성자 정보
    private Long userId;
    private String userNickname;
    private String userProfileImage;
    private Integer userLevel;
    
    // 리뷰 이미지들
    private List<String> reviewImageUrls;
    
    // 현재 사용자의 좋아요 여부
    private boolean isLikedByCurrentUser;

    public ReviewDetailDto() {}

    public ReviewDetailDto(Long reviewId, String title, String content, Integer rating, Integer likeCount,
                          Integer viewCount, Integer commentCount, LocalDateTime createdAt, Long performanceId,
                          String performanceTitle, String performanceCategory, String performanceVenue,
                          String performanceImageUrl, Long userId, String userNickname, String userProfileImage,
                          Integer userLevel, List<String> reviewImageUrls, boolean isLikedByCurrentUser) {
        this.reviewId = reviewId;
        this.title = title;
        this.content = content;
        this.rating = rating;
        this.likeCount = likeCount;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
        this.performanceId = performanceId;
        this.performanceTitle = performanceTitle;
        this.performanceCategory = performanceCategory;
        this.performanceVenue = performanceVenue;
        this.performanceImageUrl = performanceImageUrl;
        this.userId = userId;
        this.userNickname = userNickname;
        this.userProfileImage = userProfileImage;
        this.userLevel = userLevel;
        this.reviewImageUrls = reviewImageUrls;
        this.isLikedByCurrentUser = isLikedByCurrentUser;
    }

    // Getters and Setters
    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
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

    public Integer getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public List<String> getReviewImageUrls() {
        return reviewImageUrls;
    }

    public void setReviewImageUrls(List<String> reviewImageUrls) {
        this.reviewImageUrls = reviewImageUrls;
    }

    public boolean isLikedByCurrentUser() {
        return isLikedByCurrentUser;
    }

    public void setLikedByCurrentUser(boolean likedByCurrentUser) {
        isLikedByCurrentUser = likedByCurrentUser;
    }
} 