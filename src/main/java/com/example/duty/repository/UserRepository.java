package com.example.duty.repository;

import com.example.duty.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Поиск по фамилии
    List<User> findByLastNameContainingIgnoreCase(String lastName);
    
    // Поиск по должности
    List<User> findByPositionContainingIgnoreCase(String position);
    
    // Поиск по имени и фамилии
    List<User> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName, String lastName);
    
    // Кастомный запрос для поиска по всем полям
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.middleName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.position) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<User> searchUsers(@Param("searchTerm") String searchTerm);
} 