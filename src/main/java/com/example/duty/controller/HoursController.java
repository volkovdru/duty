package com.example.duty.controller;

import com.example.duty.service.HoursCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hours")
@CrossOrigin(origins = "*")
public class HoursController {
    
    @Autowired
    private HoursCalculationService hoursCalculationService;
    
    /**
     * Получить часы для пользователя на конкретную дату
     */
    @GetMapping("/user/{userId}/date/{date}")
    public ResponseEntity<Map<String, Object>> getHoursForUserAndDate(
            @PathVariable Long userId,
            @PathVariable String date) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            Map<String, Object> hours = hoursCalculationService.calculateHoursForUserAndDate(userId, localDate);
            return ResponseEntity.ok(hours);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Получить часы для пользователя за период
     */
    @GetMapping("/user/{userId}/period")
    public ResponseEntity<List<Map<String, Object>>> getHoursForUserAndPeriod(
            @PathVariable Long userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            List<Map<String, Object>> hours = hoursCalculationService.calculateHoursForUserAndPeriod(userId, start, end);
            return ResponseEntity.ok(hours);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Проверить часы Дмитрия Савинкова на 31 июля и 1 августа
     */
    @GetMapping("/savinkov-august")
    public ResponseEntity<Map<String, Object>> getSavinkovAugustHours() {
        try {
            // Предполагаем, что ID Дмитрия Савинкова = 1 (нужно уточнить)
            Long savinkovId = 1L;
            
            // Часы на 31 июля
            Map<String, Object> july31 = hoursCalculationService.calculateHoursForUserAndDate(
                    savinkovId, LocalDate.of(2024, 7, 31));
            
            // Часы на 1 августа
            Map<String, Object> august1 = hoursCalculationService.calculateHoursForUserAndDate(
                    savinkovId, LocalDate.of(2024, 8, 1));
            
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("july31", july31);
            result.put("august1", august1);
            result.put("message", "Часы рассчитаны на основе назначенных смен");
            result.put("explanation", "Система ищет смены на указанную дату и смены с предыдущего дня, которые переходят через полночь");
            result.put("note", "Если у Дмитрия есть смена 31 июля с 2-й сменой (13:00-01:00), то на 1 августа должно быть 1 час");
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> errorResult = new java.util.HashMap<>();
            errorResult.put("error", e.getMessage());
            errorResult.put("message", "Ошибка при расчете часов");
            return ResponseEntity.badRequest().body(errorResult);
        }
    }
} 