package com.team0479.myplay.dto.mission;

import java.time.LocalDateTime;

public class UserMissionDto {
    private Long id;
    private Long userId;
    private Long missionId;
    private String missionName;
    private String missionDescription;
    private String missionType;
    private Integer goal;
    private Integer progress;
    private Boolean isCompleted;
    private LocalDateTime lastUpdated;

    public UserMissionDto() {}

    public UserMissionDto(Long id, Long userId, Long missionId, String missionName, 
                         String missionDescription, String missionType, Integer goal, 
                         Integer progress, Boolean isCompleted, LocalDateTime lastUpdated) {
        this.id = id;
        this.userId = userId;
        this.missionId = missionId;
        this.missionName = missionName;
        this.missionDescription = missionDescription;
        this.missionType = missionType;
        this.goal = goal;
        this.progress = progress;
        this.isCompleted = isCompleted;
        this.lastUpdated = lastUpdated;
    }

    // 진행률 계산 메서드
    public double getProgressPercentage() {
        if (goal == null || goal == 0) return 0.0;
        return Math.min(100.0, (progress.doubleValue() / goal.doubleValue()) * 100.0);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMissionId() {
        return missionId;
    }

    public void setMissionId(Long missionId) {
        this.missionId = missionId;
    }

    public String getMissionName() {
        return missionName;
    }

    public void setMissionName(String missionName) {
        this.missionName = missionName;
    }

    public String getMissionDescription() {
        return missionDescription;
    }

    public void setMissionDescription(String missionDescription) {
        this.missionDescription = missionDescription;
    }

    public String getMissionType() {
        return missionType;
    }

    public void setMissionType(String missionType) {
        this.missionType = missionType;
    }

    public Integer getGoal() {
        return goal;
    }

    public void setGoal(Integer goal) {
        this.goal = goal;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
} 