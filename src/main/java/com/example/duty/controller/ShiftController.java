package com.example.duty.controller;

import com.example.duty.dto.ShiftDto;
import com.example.duty.service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/shifts")
@CrossOrigin(origins = "*")
public class ShiftController {
    
    @Autowired
    private ShiftService shiftService;
    
    @GetMapping
    public ResponseEntity<List<ShiftDto>> getAllShifts() {
        List<ShiftDto> shifts = shiftService.getAllShifts();
        return ResponseEntity.ok(shifts);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ShiftDto> getShiftById(@PathVariable Long id) {
        Optional<ShiftDto> shift = shiftService.getShiftById(id);
        return shift.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<ShiftDto> createShift(@Valid @RequestBody ShiftDto shiftDto) {
        ShiftDto createdShift = shiftService.createShift(shiftDto);
        return ResponseEntity.ok(createdShift);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ShiftDto> updateShift(@PathVariable Long id, @Valid @RequestBody ShiftDto shiftDto) {
        Optional<ShiftDto> updatedShift = shiftService.updateShift(id, shiftDto);
        return updatedShift.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShift(@PathVariable Long id) {
        boolean deleted = shiftService.deleteShift(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<ShiftDto>> searchShifts(@RequestParam String query) {
        List<ShiftDto> shifts = shiftService.searchShifts(query);
        return ResponseEntity.ok(shifts);
    }
} 