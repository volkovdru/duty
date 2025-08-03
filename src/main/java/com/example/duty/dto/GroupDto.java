package com.example.duty.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.Set;

public class GroupDto {
    
    private Long id;
    
    @NotBlank(message = "Название группы обязательно для заполнения")
    private String name;
    
    private Set<Long> shiftIds;
    
    // Конструкторы
    public GroupDto() {}
    
    public GroupDto(Long id, String name) {
        this.id = id;
        this.name = name;
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
    
    public Set<Long> getShiftIds() {
        return shiftIds;
    }
    
    public void setShiftIds(Set<Long> shiftIds) {
        this.shiftIds = shiftIds;
    }
    
    @Override
    public String toString() {
        return "GroupDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
} 