package com.team0479.myplay.dto.review;

import java.time.LocalDateTime;

public class CommentDto {
    private Long commentId;
    private String content;
    private LocalDateTime createdAt;
    private Long reviewId;
    private Long userId;
    private String userNickname;
    private String userProfileImage;
    private Integer userLevel;

    public CommentDto() {}

    public CommentDto(Long commentId, String content, LocalDateTime createdAt, Long reviewId,
                     Long userId, String userNickname, String userProfileImage, Integer userLevel) {
        this.commentId = commentId;
        this.content = content;
        this.createdAt = createdAt;
        this.reviewId = reviewId;
        this.userId = userId;
        this.userNickname = userNickname;
        this.userProfileImage = userProfileImage;
        this.userLevel = userLevel;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
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
} 