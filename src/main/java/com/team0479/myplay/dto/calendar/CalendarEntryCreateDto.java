package com.team0479.myplay.dto.calendar;

import java.time.LocalDateTime;

public class CalendarEntryCreateDto {
    private Integer rating;
    private String memo;
    private LocalDateTime ticketingAt;
    private LocalDateTime watchedAt;
    private String customTitle;
    private Long performanceId;
    private Long userId;

    public CalendarEntryCreateDto() {}

    public CalendarEntryCreateDto(Integer rating, String memo, LocalDateTime ticketingAt,
                                 LocalDateTime watchedAt, String customTitle, Long performanceId, Long userId) {
        this.rating = rating;
        this.memo = memo;
        this.ticketingAt = ticketingAt;
        this.watchedAt = watchedAt;
        this.customTitle = customTitle;
        this.performanceId = performanceId;
        this.userId = userId;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
} 