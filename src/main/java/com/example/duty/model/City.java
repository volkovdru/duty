package com.example.duty.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "cities")
public class City {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Название города обязательно для заполнения")
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    
    @NotNull(message = "Часовой пояс GMT обязательно для заполнения")
    @Column(name = "gmt_timezone", nullable = false)
    private String gmtTimezone;
    
    @OneToMany(mappedBy = "city", fetch = FetchType.LAZY)
    private List<User> users;
    
    // Конструкторы
    public City() {}
    
    public City(String name, String gmtTimezone) {
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
    
    public List<User> getUsers() {
        return users;
    }
    
    public void setUsers(List<User> users) {
        this.users = users;
    }
    
    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gmtTimezone='" + gmtTimezone + '\'' +
                '}';
    }
} 