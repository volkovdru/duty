package com.example.duty.service;

import com.example.duty.dto.GroupDto;
import com.example.duty.model.Group;
import com.example.duty.model.Shift;
import com.example.duty.repository.GroupRepository;
import com.example.duty.repository.ShiftRepository;
import com.example.duty.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class GroupService {
    
    @Autowired
    private GroupRepository groupRepository;
    
    @Autowired
    private ShiftRepository shiftRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public List<GroupDto> getAllGroups() {
        return groupRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public GroupDto getGroupById(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Группа не найдена"));
        return convertToDto(group);
    }
    
    public GroupDto createGroup(GroupDto groupDto) {
        if (groupRepository.existsByName(groupDto.getName())) {
            throw new RuntimeException("Группа с таким названием уже существует");
        }
        
        Group group = new Group();
        group.setName(groupDto.getName());
        
        // Устанавливаем смены, если указаны
        if (groupDto.getShiftIds() != null && !groupDto.getShiftIds().isEmpty()) {
            List<Shift> shifts = shiftRepository.findAllById(groupDto.getShiftIds());
            group.setShifts(shifts.stream().collect(java.util.stream.Collectors.toSet()));
        }
        
        Group savedGroup = groupRepository.save(group);
        return convertToDto(savedGroup);
    }
    
    public GroupDto updateGroup(Long id, GroupDto groupDto) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Группа не найдена"));
        
        // Проверяем, что новое имя не конфликтует с другими группами
        if (!group.getName().equals(groupDto.getName()) && 
            groupRepository.existsByName(groupDto.getName())) {
            throw new RuntimeException("Группа с таким названием уже существует");
        }
        
        group.setName(groupDto.getName());
        
        // Обновляем смены
        if (groupDto.getShiftIds() != null) {
            List<Shift> shifts = shiftRepository.findAllById(groupDto.getShiftIds());
            group.setShifts(shifts.stream().collect(java.util.stream.Collectors.toSet()));
        } else {
            group.setShifts(null);
        }
        
        Group savedGroup = groupRepository.save(group);
        return convertToDto(savedGroup);
    }
    
    public void deleteGroup(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Группа не найдена"));
        
        // Удаляем связь с пользователями перед удалением группы
        if (group.getUsers() != null && !group.getUsers().isEmpty()) {
            group.getUsers().forEach(user -> user.setGroup(null));
            // Сохраняем пользователей без группы
            group.getUsers().forEach(user -> userRepository.save(user));
        }
        
        groupRepository.delete(group);
    }
    
    public List<GroupDto> searchGroups(String query) {
        return groupRepository.searchGroups(query).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    private GroupDto convertToDto(Group group) {
        GroupDto dto = new GroupDto(group.getId(), group.getName());
        
        if (group.getShifts() != null) {
            dto.setShiftIds(group.getShifts().stream()
                    .map(Shift::getId)
                    .collect(java.util.stream.Collectors.toSet()));
        }
        
        return dto;
    }
} 