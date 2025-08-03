package com.example.duty.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "attendance")
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Дата обязательна для заполнения")
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id")
    private Shift shift;

    @Column(name = "is_absent")
    private Boolean isAbsent = false;

    @Column(name = "notes")
    private String notes;

    public Attendance() {}

    public Attendance(LocalDate date, User user, Shift shift) {
        this.date = date;
        this.user = user;
        this.shift = shift;
    }

    public Attendance(LocalDate date, User user, Shift shift, Boolean isAbsent, String notes) {
        this.date = date;
        this.user = user;
        this.shift = shift;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
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
        return "Attendance{" +
                "id=" + id +
                ", date=" + date +
                ", user=" + (user != null ? user.getId() : null) +
                ", shift=" + (shift != null ? shift.getId() : null) +
                ", isAbsent=" + isAbsent +
                ", notes='" + notes + '\'' +
                '}';
    }
} 