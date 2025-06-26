package com.team0479.myplay.dto.review;

import java.util.List;

public class ReviewCreateDto {
    private String title;
    private String content;
    private Integer rating;
    private Long performanceId;
    private Long userId;
    private List<String> imageUrls; // 리뷰 이미지 URL들

    public ReviewCreateDto() {}

    public ReviewCreateDto(String title, String content, Integer rating, Long performanceId, Long userId, List<String> imageUrls) {
        this.title = title;
        this.content = content;
        this.rating = rating;
        this.performanceId = performanceId;
        this.userId = userId;
        this.imageUrls = imageUrls;
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

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
} 