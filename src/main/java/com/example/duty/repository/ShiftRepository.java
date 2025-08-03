package com.example.duty.repository;

import com.example.duty.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {
    
    List<Shift> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT s FROM Shift s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Shift> searchShifts(@Param("query") String query);
    
    boolean existsByName(String name);
} 