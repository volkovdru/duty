package com.example.duty.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CityDto {
    
    private Long id;
    
    @NotBlank(message = "Название города обязательно для заполнения")
    private String name;
    
    @NotNull(message = "Часовой пояс GMT обязательно для заполнения")
    private String gmtTimezone;
    
    // Конструкторы
    public CityDto() {}
    
    public CityDto(Long id, String name, String gmtTimezone) {
        this.id = id;
        this.name = name;
        this.gmtTimezone = gmtTimezone;
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
    
    public String getGmtTimezone() {
        return gmtTimezone;
    }
    
    public void setGmtTimezone(String gmtTimezone) {
        this.gmtTimezone = gmtTimezone;
    }
    
    @Override
    public String toString() {
        return "CityDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gmtTimezone='" + gmtTimezone + '\'' +
                '}';
    }
} 