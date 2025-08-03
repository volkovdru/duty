package com.example.duty.repository;

import com.example.duty.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    
    List<Group> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT g FROM Group g WHERE LOWER(g.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Group> searchGroups(@Param("query") String query);
    
    boolean existsByName(String name);
} 