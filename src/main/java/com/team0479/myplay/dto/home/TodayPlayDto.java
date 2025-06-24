package com.team0479.myplay.dto.home;

import java.time.LocalDate;

public class TodayPlayDto {
    private Long performanceId;
    private String title;
    private String category;
    private String venue;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private String imageUrl;
    private Integer ranking;
    private String recommendReason; // 추천 이유 (ex: "뮤지컬을 5번 관람하셨어요!")

    public TodayPlayDto() {}

    public TodayPlayDto(Long performanceId, String title, String category, String venue, 
                       LocalDate startDate, LocalDate endDate, String description, String imageUrl, 
                       Integer ranking, String recommendReason) {
        this.performanceId = performanceId;
        this.title = title;
        this.category = category;
        this.venue = venue;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.imageUrl = imageUrl;
        this.ranking = ranking;
        this.recommendReason = recommendReason;
    }

    public Long getPerformanceId() {
        return performanceId;
    }

    public void setPerformanceId(Long performanceId) {
        this.performanceId = performanceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public String getRecommendReason() {
        return recommendReason;
    }

    public void setRecommendReason(String recommendReason) {
        this.recommendReason = recommendReason;
    }
} 