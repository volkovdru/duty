package com.example.duty.controller;

import com.example.duty.dto.AttendanceDto;
import com.example.duty.service.AttendanceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "*")
public class AttendanceController {
    
    @Autowired
    private AttendanceService attendanceService;
    
    @GetMapping
    public ResponseEntity<List<AttendanceDto>> getAllAttendance() {
        List<AttendanceDto> attendance = attendanceService.getAllAttendance();
        return ResponseEntity.ok(attendance);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AttendanceDto> getAttendanceById(@PathVariable Long id) {
        try {
            AttendanceDto attendance = attendanceService.getAttendanceById(id);
            return ResponseEntity.ok(attendance);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<?> createAttendance(@Valid @RequestBody AttendanceDto attendanceDto) {
        try {
            AttendanceDto createdAttendance = attendanceService.createAttendance(attendanceDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAttendance);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAttendance(@PathVariable Long id, @Valid @RequestBody AttendanceDto attendanceDto) {
        try {
            AttendanceDto updatedAttendance = attendanceService.updateAttendance(id, attendanceDto);
            return ResponseEntity.ok(updatedAttendance);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAttendance(@PathVariable Long id) {
        try {
            attendanceService.deleteAttendance(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/date/{date}")
    public ResponseEntity<List<AttendanceDto>> getAttendanceByDate(@PathVariable String date) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            List<AttendanceDto> attendance = attendanceService.getAttendanceByDate(localDate);
            return ResponseEntity.ok(attendance);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AttendanceDto>> getAttendanceByUserId(@PathVariable Long userId) {
        List<AttendanceDto> attendance = attendanceService.getAttendanceByUserId(userId);
        return ResponseEntity.ok(attendance);
    }
    
    @GetMapping("/user/{userId}/range")
    public ResponseEntity<List<AttendanceDto>> getAttendanceByUserIdAndDateRange(
            @PathVariable Long userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            List<AttendanceDto> attendance = attendanceService.getAttendanceByUserIdAndDateRange(userId, start, end);
            return ResponseEntity.ok(attendance);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/range")
    public ResponseEntity<List<AttendanceDto>> getAttendanceByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            List<AttendanceDto> attendance = attendanceService.getAttendanceByDateRange(start, end);
            return ResponseEntity.ok(attendance);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/user/{userId}/date/{date}")
    public ResponseEntity<AttendanceDto> getAttendanceByUserIdAndDate(
            @PathVariable Long userId,
            @PathVariable String date) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            AttendanceDto attendance = attendanceService.getAttendanceByUserIdAndDate(userId, localDate);
            return attendance != null ? ResponseEntity.ok(attendance) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/debug")
    public ResponseEntity<String> logDebugInfo(@RequestBody String debugInfo) {
        System.out.println("=== DEBUG INFO ===");
        System.out.println(debugInfo);
        System.out.println("==================");
        return ResponseEntity.ok("Debug info logged");
    }
} 