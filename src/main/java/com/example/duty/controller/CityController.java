package com.example.duty.controller;

import com.example.duty.dto.CityDto;
import com.example.duty.service.CityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cities")
@CrossOrigin(origins = "*")
public class CityController {
    
    @Autowired
    private CityService cityService;
    
    @GetMapping
    public ResponseEntity<List<CityDto>> getAllCities() {
        List<CityDto> cities = cityService.getAllCities();
        return ResponseEntity.ok(cities);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CityDto> getCityById(@PathVariable Long id) {
        try {
            CityDto city = cityService.getCityById(id);
            return ResponseEntity.ok(city);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<?> createCity(@Valid @RequestBody CityDto cityDto) {
        try {
            CityDto createdCity = cityService.createCity(cityDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCity);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCity(@PathVariable Long id, @Valid @RequestBody CityDto cityDto) {
        try {
            CityDto updatedCity = cityService.updateCity(id, cityDto);
            return ResponseEntity.ok(updatedCity);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCity(@PathVariable Long id) {
        try {
            cityService.deleteCity(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<CityDto>> searchCities(@RequestParam String query) {
        List<CityDto> cities = cityService.searchCities(query);
        return ResponseEntity.ok(cities);
    }
} 