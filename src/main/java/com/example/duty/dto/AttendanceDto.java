package com.example.duty.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class AttendanceDto {
    private Long id;
    
    @NotNull(message = "Дата обязательна для заполнения")
    private LocalDate date;
    
    @NotNull(message = "ID пользователя обязателен для заполнения")
    private Long userId;
    
    private String userName;
    
    private Long shiftId;
    
    private String shiftName;
    
    private Boolean isAbsent = false;
    
    private String notes;

    public AttendanceDto() {}

    public AttendanceDto(Long id, LocalDate date, Long userId, String userName, Long shiftId, String shiftName, Boolean isAbsent, String notes) {
        this.id = id;
        this.date = date;
        this.userId = userId;
        this.userName = userName;
        this.shiftId = shiftId;
        this.shiftName = shiftName;
        this.isAbsent = isAbsent;
        this.notes = notes;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getShiftId() {
        return shiftId;
    }

    public void setShiftId(Long shiftId) {
        this.shiftId = shiftId;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public Boolean getIsAbsent() {
        return isAbsent;
    }

    public void setIsAbsent(Boolean isAbsent) {
        this.isAbsent = isAbsent;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "AttendanceDto{" +
                "id=" + id +
                ", date=" + date +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", shiftId=" + shiftId +
                ", shiftName='" + shiftName + '\'' +
                ", isAbsent=" + isAbsent +
                ", notes='" + notes + '\'' +
                '}';
    }
} 