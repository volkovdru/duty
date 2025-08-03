package com.example.duty.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class WorkHoursDto {
    private Long id;
    
    @NotNull(message = "Дата обязательна для заполнения")
    private LocalDate date;
    
    @NotNull(message = "ID пользователя обязателен для заполнения")
    private Long userId;
    
    private String userName;
    
    private Double dayHours = 0.0;
    
    private Double nightHours = 0.0;
    
    private Double totalHours = 0.0;
    
    private Double overtimeHours = 0.0;
    
    private String notes;

    public WorkHoursDto() {}

    public WorkHoursDto(Long id, LocalDate date, Long userId, String userName, Double dayHours, Double nightHours, Double totalHours, Double overtimeHours, String notes) {
        this.id = id;
        this.date = date;
        this.userId = userId;
        this.userName = userName;
        this.dayHours = dayHours;
        this.nightHours = nightHours;
        this.totalHours = totalHours;
        this.overtimeHours = overtimeHours;
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

    public Double getDayHours() {
        return dayHours;
    }

    public void setDayHours(Double dayHours) {
        this.dayHours = dayHours;
    }

    public Double getNightHours() {
        return nightHours;
    }

    public void setNightHours(Double nightHours) {
        this.nightHours = nightHours;
    }

    public Double getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(Double totalHours) {
        this.totalHours = totalHours;
    }

    public Double getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(Double overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "WorkHoursDto{" +
                "id=" + id +
                ", date=" + date +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", dayHours=" + dayHours +
                ", nightHours=" + nightHours +
                ", totalHours=" + totalHours +
                ", overtimeHours=" + overtimeHours +
                ", notes='" + notes + '\'' +
                '}';
    }
} 