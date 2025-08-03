package com.example.duty.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.Set;

public class ShiftDto {
    
    private Long id;
    
    @NotBlank(message = "Название смены обязательно для заполнения")
    private String name;
    
    @NotNull(message = "Время начала обязательно для заполнения")
    private LocalTime startTime;
    
    @NotNull(message = "Время окончания обязательно для заполнения")
    private LocalTime endTime;
    
    private Set<Long> groupIds;
    private Set<String> groupNames;
    
    // Конструкторы
    public ShiftDto() {}
    
    public ShiftDto(Long id, String name, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    // Геттеры и сеттеры
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
    
    public LocalTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
    
    public Set<Long> getGroupIds() {
        return groupIds;
    }
    
    public void setGroupIds(Set<Long> groupIds) {
        this.groupIds = groupIds;
    }
    
    public Set<String> getGroupNames() {
        return groupNames;
    }
    
    public void setGroupNames(Set<String> groupNames) {
        this.groupNames = groupNames;
    }
    
    @Override
    public String toString() {
        return "ShiftDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
} 