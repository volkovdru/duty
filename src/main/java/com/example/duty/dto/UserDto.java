package com.example.duty.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class UserDto {
    
    private Long id;
    
    @NotBlank(message = "Имя обязательно для заполнения")
    private String firstName;
    
    @NotBlank(message = "Фамилия обязательна для заполнения")
    private String lastName;
    
    private String middleName;
    
    @NotNull(message = "Дата рождения обязательна для заполнения")
    private LocalDate birthDate;
    
    @NotBlank(message = "Должность обязательна для заполнения")
    private String position;
    
    // Конструкторы
    public UserDto() {}
    
    public UserDto(Long id, String firstName, String lastName, String middleName, LocalDate birthDate, String position) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.position = position;
    }
    
    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getMiddleName() {
        return middleName;
    }
    
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    
    public LocalDate getBirthDate() {
        return birthDate;
    }
    
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
    
    public String getPosition() {
        return position;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
    
    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", birthDate=" + birthDate +
                ", position='" + position + '\'' +
                '}';
    }
} 