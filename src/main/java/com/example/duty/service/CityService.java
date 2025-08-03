package com.example.duty.service;

import com.example.duty.dto.CityDto;
import com.example.duty.model.City;
import com.example.duty.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CityService {
    
    @Autowired
    private CityRepository cityRepository;
    
    public List<CityDto> getAllCities() {
        return cityRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public CityDto getCityById(Long id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Город не найден"));
        return convertToDto(city);
    }
    
    public CityDto createCity(CityDto cityDto) {
        if (cityRepository.existsByName(cityDto.getName())) {
            throw new RuntimeException("Город с таким названием уже существует");
        }
        
        City city = new City();
        city.setName(cityDto.getName());
        city.setGmtTimezone(cityDto.getGmtTimezone());
        
        City savedCity = cityRepository.save(city);
        return convertToDto(savedCity);
    }
    
    public CityDto updateCity(Long id, CityDto cityDto) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Город не найден"));
        
        // Проверяем, что новое имя не конфликтует с другими городами
        if (!city.getName().equals(cityDto.getName()) && 
            cityRepository.existsByName(cityDto.getName())) {
            throw new RuntimeException("Город с таким названием уже существует");
        }
        
        city.setName(cityDto.getName());
        city.setGmtTimezone(cityDto.getGmtTimezone());
        
        City savedCity = cityRepository.save(city);
        return convertToDto(savedCity);
    }
    
    public void deleteCity(Long id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Город не найден"));
        
        // Удаляем связь с пользователями перед удалением города
        if (city.getUsers() != null && !city.getUsers().isEmpty()) {
            city.getUsers().forEach(user -> user.setCity(null));
        }
        
        cityRepository.delete(city);
    }
    
    public List<CityDto> searchCities(String query) {
        return cityRepository.searchCities(query).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    private CityDto convertToDto(City city) {
        return new CityDto(city.getId(), city.getName(), city.getGmtTimezone());
    }
} 