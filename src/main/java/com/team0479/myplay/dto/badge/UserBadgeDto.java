package com.team0479.myplay.dto.badge;

import java.time.LocalDateTime;

public class UserBadgeDto {
    private Long badgeId;
    private String badgeName;
    private String title;
    private String description;
    private Boolean isAcquired;
    private LocalDateTime acquiredAt;

    public UserBadgeDto() {}

    public UserBadgeDto(Long badgeId, String badgeName, String title, String description, 
                       Boolean isAcquired, LocalDateTime acquiredAt) {
        this.badgeId = badgeId;
        this.badgeName = badgeName;
        this.title = title;
        this.description = description;
        this.isAcquired = isAcquired;
        this.acquiredAt = acquiredAt;
    }

    // Getters and Setters
    public Long getBadgeId() {
        return badgeId;
    }

    public void setBadgeId(Long badgeId) {
        this.badgeId = badgeId;
    }

    public String getBadgeName() {
        return badgeName;
    }

    public void setBadgeName(String badgeName) {
        this.badgeName = badgeName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsAcquired() {
        return isAcquired;
    }

    public void setIsAcquired(Boolean isAcquired) {
        this.isAcquired = isAcquired;
    }

    public LocalDateTime getAcquiredAt() {
        return acquiredAt;
    }

    public void setAcquiredAt(LocalDateTime acquiredAt) {
        this.acquiredAt = acquiredAt;
    }
} 