package com.example.duty.service;

import com.example.duty.model.Attendance;
import com.example.duty.model.Shift;
import com.example.duty.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HoursCalculationService {
    
    @Autowired
    private AttendanceRepository attendanceRepository;
    
    /**
     * Рассчитывает часы для пользователя на конкретную дату
     */
    public Map<String, Object> calculateHoursForUserAndDate(Long userId, LocalDate date) {
        // Ищем смены на эту дату
        List<Attendance> attendancesOnDate = attendanceRepository.findByUserIdAndDateBetween(userId, date, date);
        
        // Ищем смены на предыдущий день, которые могут переходить через полночь
        List<Attendance> attendancesPreviousDay = attendanceRepository.findByUserIdAndDateBetween(
                userId, date.minusDays(1), date.minusDays(1));
        
        double totalDayHours = 0.0;
        double totalNightHours = 0.0;
        double totalHours = 0.0;
        
        // Обрабатываем смены на текущую дату
        for (Attendance attendance : attendancesOnDate) {
            if (attendance.getShift() != null && !attendance.getIsAbsent()) {
                Shift shift = attendance.getShift();
                LocalTime startTime = shift.getStartTime();
                LocalTime endTime = shift.getEndTime();
                
                // Рассчитываем часы для этой смены
                Map<String, Double> shiftHours = calculateHoursFromShift(startTime, endTime, date);
                
                totalDayHours += shiftHours.get("dayHours");
                totalNightHours += shiftHours.get("nightHours");
                totalHours += shiftHours.get("totalHours");
            }
        }
        
        // Обрабатываем смены с предыдущего дня, которые переходят через полночь
        for (Attendance attendance : attendancesPreviousDay) {
            if (attendance.getShift() != null && !attendance.getIsAbsent()) {
                Shift shift = attendance.getShift();
                LocalTime startTime = shift.getStartTime();
                LocalTime endTime = shift.getEndTime();
                
                // Если смена переходит через полночь (endTime < startTime)
                if (endTime.isBefore(startTime)) {
                    // Рассчитываем только часы, которые приходятся на текущую дату (с 00:00 до endTime)
                    Map<String, Double> overnightHours = calculateOvernightHoursForDate(startTime, endTime, date);
                    
                    totalDayHours += overnightHours.get("dayHours");
                    totalNightHours += overnightHours.get("nightHours");
                    totalHours += overnightHours.get("totalHours");
                }
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("date", date);
        result.put("userId", userId);
        result.put("dayHours", totalDayHours);
        result.put("nightHours", totalNightHours);
        result.put("totalHours", totalHours);
        result.put("debug", String.format("Найдено смен на %s: %d, смен с предыдущего дня: %d", 
                date, attendancesOnDate.size(), attendancesPreviousDay.size()));
        
        return result;
    }
    
    /**
     * Рассчитывает часы для пользователя за период
     */
    public List<Map<String, Object>> calculateHoursForUserAndPeriod(Long userId, LocalDate startDate, LocalDate endDate) {
        List<Attendance> attendances = attendanceRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
        
        Map<LocalDate, Map<String, Double>> dailyHours = new HashMap<>();
        
        for (Attendance attendance : attendances) {
            if (attendance.getShift() != null && !attendance.getIsAbsent()) {
                Shift shift = attendance.getShift();
                LocalTime startTime = shift.getStartTime();
                LocalTime endTime = shift.getEndTime();
                LocalDate shiftDate = attendance.getDate();
                
                // Рассчитываем часы для этой смены
                Map<String, Double> shiftHours = calculateHoursFromShift(startTime, endTime, shiftDate);
                
                // Добавляем к существующим часам на эту дату
                dailyHours.merge(shiftDate, shiftHours, (existing, newHours) -> {
                    Map<String, Double> combined = new HashMap<>();
                    combined.put("dayHours", existing.get("dayHours") + newHours.get("dayHours"));
                    combined.put("nightHours", existing.get("nightHours") + newHours.get("nightHours"));
                    combined.put("totalHours", existing.get("totalHours") + newHours.get("totalHours"));
                    return combined;
                });
            }
        }
        
        // Преобразуем в список результатов
        return dailyHours.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("date", entry.getKey());
                    result.put("userId", userId);
                    result.put("dayHours", entry.getValue().get("dayHours"));
                    result.put("nightHours", entry.getValue().get("nightHours"));
                    result.put("totalHours", entry.getValue().get("totalHours"));
                    return result;
                })
                .toList();
    }
    
    /**
     * Рассчитывает часы для конкретной смены
     */
    private Map<String, Double> calculateHoursFromShift(LocalTime startTime, LocalTime endTime, LocalDate shiftDate) {
        double totalHours;
        double dayHours;
        double nightHours;
        
        if (endTime.isAfter(startTime)) {
            // Смена в пределах одного дня
            totalHours = ChronoUnit.MINUTES.between(startTime, endTime) / 60.0;
            dayHours = calculateDayHours(startTime, endTime);
            nightHours = totalHours - dayHours;
        } else {
            // Смена переходит через полночь
            totalHours = ChronoUnit.MINUTES.between(startTime, LocalTime.MAX) / 60.0;
            totalHours += ChronoUnit.MINUTES.between(LocalTime.MIN, endTime) / 60.0;
            
            // Рассчитываем часы для первого дня
            double firstDayHours = calculateDayHours(startTime, LocalTime.MAX);
            double firstNightHours = calculateNightHours(startTime, LocalTime.MAX);
            
            // Рассчитываем часы для второго дня
            double secondDayHours = calculateDayHours(LocalTime.MIN, endTime);
            double secondNightHours = calculateNightHours(LocalTime.MIN, endTime);
            
            dayHours = firstDayHours + secondDayHours;
            nightHours = firstNightHours + secondNightHours;
        }
        
        Map<String, Double> result = new HashMap<>();
        result.put("totalHours", totalHours);
        result.put("dayHours", dayHours);
        result.put("nightHours", nightHours);
        
        return result;
    }
    
    /**
     * Рассчитывает дневные часы (6:00 - 22:00)
     */
    private double calculateDayHours(LocalTime startTime, LocalTime endTime) {
        LocalTime dayStart = LocalTime.of(6, 0);
        LocalTime dayEnd = LocalTime.of(22, 0);
        
        LocalTime effectiveStart = startTime.isBefore(dayStart) ? dayStart : startTime;
        LocalTime effectiveEnd = endTime.isAfter(dayEnd) ? dayEnd : endTime;
        
        if (effectiveStart.isBefore(effectiveEnd)) {
            return ChronoUnit.MINUTES.between(effectiveStart, effectiveEnd) / 60.0;
        }
        
        return 0.0;
    }
    
    /**
     * Рассчитывает ночные часы (22:00 - 6:00)
     */
    private double calculateNightHours(LocalTime startTime, LocalTime endTime) {
        LocalTime nightStart = LocalTime.of(22, 0);
        LocalTime nightEnd = LocalTime.of(6, 0);
        
        LocalTime effectiveStart = startTime.isBefore(nightStart) ? nightStart : startTime;
        LocalTime effectiveEnd = endTime.isAfter(nightEnd) ? nightEnd : endTime;
        
        if (effectiveStart.isBefore(effectiveEnd)) {
            return ChronoUnit.MINUTES.between(effectiveStart, effectiveEnd) / 60.0;
        }
        
        return 0.0;
    }
    
    /**
     * Рассчитывает часы для смены, переходящей через полночь, на конкретную дату
     */
    private Map<String, Double> calculateOvernightHours(LocalTime startTime, LocalTime endTime, LocalDate targetDate) {
        // Рассчитываем часы с 00:00 до времени окончания смены
        double totalHours = ChronoUnit.MINUTES.between(LocalTime.MIN, endTime) / 60.0;
        double dayHours = calculateDayHours(LocalTime.MIN, endTime);
        double nightHours = calculateNightHours(LocalTime.MIN, endTime);
        
        Map<String, Double> result = new HashMap<>();
        result.put("totalHours", totalHours);
        result.put("dayHours", dayHours);
        result.put("nightHours", nightHours);
        
        return result;
    }
    
    /**
     * Рассчитывает часы для смены, переходящей через полночь, на конкретную дату
     */
    private Map<String, Double> calculateOvernightHoursForDate(LocalTime startTime, LocalTime endTime, LocalDate targetDate) {
        // Рассчитываем часы с 00:00 до времени окончания смены
        double totalHours = ChronoUnit.MINUTES.between(LocalTime.MIN, endTime) / 60.0;
        double dayHours = calculateDayHours(LocalTime.MIN, endTime);
        double nightHours = calculateNightHours(LocalTime.MIN, endTime);
        
        Map<String, Double> result = new HashMap<>();
        result.put("totalHours", totalHours);
        result.put("dayHours", dayHours);
        result.put("nightHours", nightHours);
        
        return result;
    }
} 