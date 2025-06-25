package com.team0479.myplay.dto.calendar;

import java.time.LocalDateTime;

public class CalendarEntryDto {
    private Long id;
    private Integer rating;
    private String memo;
    private LocalDateTime ticketingAt;
    private LocalDateTime watchedAt;
    private LocalDateTime createdAt;
    private String customTitle;
    private Long performanceId;
    private String performanceTitle;
    private String performanceCategory;
    private String performanceVenue;
    private LocalDateTime performanceStartDate;
    private LocalDateTime performanceEndDate;
    private Long userId;

    public CalendarEntryDto() {}

    public CalendarEntryDto(Long id, Integer rating, String memo, LocalDateTime ticketingAt,
                           LocalDateTime watchedAt, LocalDateTime createdAt, String customTitle,
                           Long performanceId, String performanceTitle, String performanceCategory,
                           String performanceVenue, LocalDateTime performanceStartDate, 
                           LocalDateTime performanceEndDate, Long userId) {
        this.id = id;
        this.rating = rating;
        this.memo = memo;
        this.ticketingAt = ticketingAt;
        this.watchedAt = watchedAt;
        this.createdAt = createdAt;
        this.customTitle = customTitle;
        this.performanceId = performanceId;
        this.performanceTitle = performanceTitle;
        this.performanceCategory = performanceCategory;
        this.performanceVenue = performanceVenue;
        this.performanceStartDate = performanceStartDate;
        this.performanceEndDate = performanceEndDate;
        this.userId = userId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public LocalDateTime getTicketingAt() {
        return ticketingAt;
    }

    public void setTicketingAt(LocalDateTime ticketingAt) {
        this.ticketingAt = ticketingAt;
    }

    public LocalDateTime getWatchedAt() {
        return watchedAt;
    }

    public void setWatchedAt(LocalDateTime watchedAt) {
        this.watchedAt = watchedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCustomTitle() {
        return customTitle;
    }

    public void setCustomTitle(String customTitle) {
        this.customTitle = customTitle;
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

    public LocalDateTime getPerformanceStartDate() {
        return performanceStartDate;
    }

    public void setPerformanceStartDate(LocalDateTime performanceStartDate) {
        this.performanceStartDate = performanceStartDate;
    }

    public LocalDateTime getPerformanceEndDate() {
        return performanceEndDate;
    }

    public void setPerformanceEndDate(LocalDateTime performanceEndDate) {
        this.performanceEndDate = performanceEndDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
} 