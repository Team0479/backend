package com.team0479.myplay.domain.review;

import java.time.LocalDateTime;

public class Review {
    private Long id;
    private String content;
    private Integer rating;
    private Integer likeCount;
    private Integer viewCount;
    private LocalDateTime createdAt;
    private Long performanceId;
    private Long userId;

    public Review() {}

    public Review(Long id, String content, Integer rating, Integer likeCount, Integer viewCount,
                 LocalDateTime createdAt, Long performanceId, Long userId) {
        this.id = id;
        this.content = content;
        this.rating = rating;
        this.likeCount = likeCount;
        this.viewCount = viewCount;
        this.createdAt = createdAt;
        this.performanceId = performanceId;
        this.userId = userId;
    }

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

    public Long getPerformanceId() {
        return performanceId;
    }

    public void setPerformanceId(Long performanceId) {
        this.performanceId = performanceId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
} 