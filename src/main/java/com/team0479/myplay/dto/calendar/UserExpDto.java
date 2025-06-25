package com.team0479.myplay.dto.calendar;

public class UserExpDto {
    private Long userId;
    private int currentExp;
    private int level;
    private int expToNextLevel;
    private int expGained;

    public UserExpDto() {}

    public UserExpDto(Long userId, int currentExp, int level, int expToNextLevel, int expGained) {
        this.userId = userId;
        this.currentExp = currentExp;
        this.level = level;
        this.expToNextLevel = expToNextLevel;
        this.expGained = expGained;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getCurrentExp() {
        return currentExp;
    }

    public void setCurrentExp(int currentExp) {
        this.currentExp = currentExp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExpToNextLevel() {
        return expToNextLevel;
    }

    public void setExpToNextLevel(int expToNextLevel) {
        this.expToNextLevel = expToNextLevel;
    }

    public int getExpGained() {
        return expGained;
    }

    public void setExpGained(int expGained) {
        this.expGained = expGained;
    }
} 