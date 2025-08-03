package com.example.duty.service;

import com.example.duty.dto.UserDto;
import com.example.duty.model.User;
import com.example.duty.model.Group;
import com.example.duty.model.City;
import com.example.duty.repository.UserRepository;
import com.example.duty.repository.GroupRepository;
import com.example.duty.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private GroupRepository groupRepository;
    
    @Autowired
    private CityRepository cityRepository;
    
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::convertToDto);
    }
    
    public UserDto createUser(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setMiddleName(userDto.getMiddleName());
        user.setBirthDate(userDto.getBirthDate());
        user.setPosition(userDto.getPosition());
        
        // Устанавливаем группу, если указана
        if (userDto.getGroupId() != null) {
            Group group = groupRepository.findById(userDto.getGroupId())
                    .orElseThrow(() -> new RuntimeException("Группа не найдена"));
            user.setGroup(group);
        }
        
        if (userDto.getCityId() != null) {
            City city = cityRepository.findById(userDto.getCityId())
                    .orElseThrow(() -> new RuntimeException("Город не найден"));
            user.setCity(city);
        }
        
        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }
    
    public Optional<UserDto> updateUser(Long id, UserDto userDto) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setFirstName(userDto.getFirstName());
                    user.setLastName(userDto.getLastName());
                    user.setMiddleName(userDto.getMiddleName());
                    user.setBirthDate(userDto.getBirthDate());
                    user.setPosition(userDto.getPosition());
                    
                    // Обновляем группу
                    if (userDto.getGroupId() != null) {
                        Group group = groupRepository.findById(userDto.getGroupId())
                                .orElseThrow(() -> new RuntimeException("Группа не найдена"));
                        user.setGroup(group);
                    } else {
                        user.setGroup(null);
                    }
                    
                    // Обновляем город
                    if (userDto.getCityId() != null) {
                        City city = cityRepository.findById(userDto.getCityId())
                                .orElseThrow(() -> new RuntimeException("Город не найден"));
                        user.setCity(city);
                    } else {
                        user.setCity(null);
                    }
                    
                    User savedUser = userRepository.save(user);
                    return convertToDto(savedUser);
                });
    }
    
    public boolean deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            return true;
        }
        return false;
    }
    
    public List<UserDto> searchUsers(String query) {
        return userRepository.searchUsers(query).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setMiddleName(user.getMiddleName());
        dto.setBirthDate(user.getBirthDate());
        dto.setPosition(user.getPosition());
        
        if (user.getGroup() != null) {
            dto.setGroupId(user.getGroup().getId());
            dto.setGroupName(user.getGroup().getName());
        }
        
        if (user.getCity() != null) {
            dto.setCityId(user.getCity().getId());
            dto.setCityName(user.getCity().getName());
        }
        
        return dto;
    }
} 