package com.example.duty.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "groups")
public class Group {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Название группы обязательно для заполнения")
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    
    @OneToMany(mappedBy = "group", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<User> users;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "group_shifts",
        joinColumns = @JoinColumn(name = "group_id"),
        inverseJoinColumns = @JoinColumn(name = "shift_id")
    )
    private Set<Shift> shifts;
    
    // Конструкторы
    public Group() {}
    
    public Group(String name) {
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
    
    public List<User> getUsers() {
        return users;
    }
    
    public void setUsers(List<User> users) {
        this.users = users;
    }
    
    public Set<Shift> getShifts() {
        return shifts;
    }
    
    public void setShifts(Set<Shift> shifts) {
        this.shifts = shifts;
    }
    
    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
} 