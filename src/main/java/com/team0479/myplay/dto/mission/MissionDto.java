package com.team0479.myplay.dto.mission;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MissionDto {
    private Long id;
    private String name;
    private String description;
    private String type;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer goal;
    private String targetAction;
    private LocalDateTime createdAt;

    public MissionDto() {}

    public MissionDto(Long id, String name, String description, String type, 
                     LocalDate startDate, LocalDate endDate, Integer goal, 
                     String targetAction, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.goal = goal;
        this.targetAction = targetAction;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Integer getGoal() {
        return goal;
    }

    public void setGoal(Integer goal) {
        this.goal = goal;
    }

    public String getTargetAction() {
        return targetAction;
    }

    public void setTargetAction(String targetAction) {
        this.targetAction = targetAction;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
} 