package com.example.duty.service;

import com.example.duty.dto.AttendanceDto;
import com.example.duty.model.Attendance;
import com.example.duty.model.User;
import com.example.duty.model.Shift;
import com.example.duty.repository.AttendanceRepository;
import com.example.duty.repository.UserRepository;
import com.example.duty.repository.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AttendanceService {
    
    @Autowired
    private AttendanceRepository attendanceRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ShiftRepository shiftRepository;
    
    public List<AttendanceDto> getAllAttendance() {
        return attendanceRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public AttendanceDto getAttendanceById(Long id) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Запись о посещаемости не найдена"));
        return convertToDto(attendance);
    }
    
    public AttendanceDto createAttendance(AttendanceDto attendanceDto) {
        User user = userRepository.findById(attendanceDto.getUserId())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        
        Shift shift = null;
        if (attendanceDto.getShiftId() != null) {
            shift = shiftRepository.findById(attendanceDto.getShiftId())
                    .orElseThrow(() -> new RuntimeException("Смена не найдена"));
        }
        
        Attendance attendance = new Attendance(
                attendanceDto.getDate(),
                user,
                shift,
                attendanceDto.getIsAbsent(),
                attendanceDto.getNotes()
        );
        
        Attendance savedAttendance = attendanceRepository.save(attendance);
        return convertToDto(savedAttendance);
    }
    
    public AttendanceDto updateAttendance(Long id, AttendanceDto attendanceDto) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Запись о посещаемости не найдена"));
        
        User user = userRepository.findById(attendanceDto.getUserId())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        
        Shift shift = null;
        if (attendanceDto.getShiftId() != null) {
            shift = shiftRepository.findById(attendanceDto.getShiftId())
                    .orElseThrow(() -> new RuntimeException("Смена не найдена"));
        }
        
        attendance.setDate(attendanceDto.getDate());
        attendance.setUser(user);
        attendance.setShift(shift);
        attendance.setIsAbsent(attendanceDto.getIsAbsent());
        attendance.setNotes(attendanceDto.getNotes());
        
        Attendance savedAttendance = attendanceRepository.save(attendance);
        return convertToDto(savedAttendance);
    }
    
    public void deleteAttendance(Long id) {
        attendanceRepository.deleteById(id);
    }
    
    public List<AttendanceDto> getAttendanceByDate(LocalDate date) {
        return attendanceRepository.findByDate(date).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<AttendanceDto> getAttendanceByUserId(Long userId) {
        return attendanceRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<AttendanceDto> getAttendanceByUserIdAndDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.findByUserIdAndDateBetween(userId, startDate, endDate).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<AttendanceDto> getAttendanceByDateRange(LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.findByDateBetween(startDate, endDate).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public AttendanceDto getAttendanceByUserIdAndDate(Long userId, LocalDate date) {
        Attendance attendance = attendanceRepository.findByUserIdAndDate(userId, date);
        return attendance != null ? convertToDto(attendance) : null;
    }
    
    private AttendanceDto convertToDto(Attendance attendance) {
        return new AttendanceDto(
                attendance.getId(),
                attendance.getDate(),
                attendance.getUser().getId(),
                attendance.getUser().getFirstName() + " " + attendance.getUser().getLastName(),
                attendance.getShift() != null ? attendance.getShift().getId() : null,
                attendance.getShift() != null ? attendance.getShift().getName() : null,
                attendance.getIsAbsent(),
                attendance.getNotes()
        );
    }
} 