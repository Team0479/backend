package com.team0479.myplay.dto.review;

public class CommentCreateDto {
    private String content;
    private Long reviewId;
    private Long userId;

    public CommentCreateDto() {}

    public CommentCreateDto(String content, Long reviewId, Long userId) {
        this.content = content;
        this.reviewId = reviewId;
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
} 