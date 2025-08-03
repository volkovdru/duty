package com.example.duty.service;

import com.example.duty.dto.ShiftDto;
import com.example.duty.model.Shift;
import com.example.duty.model.Group;
import com.example.duty.repository.ShiftRepository;
import com.example.duty.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ShiftService {
    
    @Autowired
    private ShiftRepository shiftRepository;
    
    @Autowired
    private GroupRepository groupRepository;
    
    public List<ShiftDto> getAllShifts() {
        return shiftRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public Optional<ShiftDto> getShiftById(Long id) {
        return shiftRepository.findById(id)
                .map(this::convertToDto);
    }
    
    public ShiftDto createShift(ShiftDto shiftDto) {
        Shift shift = new Shift();
        shift.setName(shiftDto.getName());
        shift.setStartTime(shiftDto.getStartTime());
        shift.setEndTime(shiftDto.getEndTime());
        
        // Сначала сохраняем смену
        Shift savedShift = shiftRepository.save(shift);
        
        // Устанавливаем группы, если указаны
        if (shiftDto.getGroupIds() != null && !shiftDto.getGroupIds().isEmpty()) {
            List<Group> groups = groupRepository.findAllById(shiftDto.getGroupIds());
            savedShift.setGroups(groups);
            
            // Добавляем смену в группы (синхронизируем двунаправленную связь)
            for (Group group : groups) {
                if (group.getShifts() == null) {
                    group.setShifts(new java.util.HashSet<>());
                }
                group.getShifts().add(savedShift);
                groupRepository.save(group);
            }
            
            savedShift = shiftRepository.save(savedShift);
        }
        
        return convertToDto(savedShift);
    }
    
    public Optional<ShiftDto> updateShift(Long id, ShiftDto shiftDto) {
        return shiftRepository.findById(id)
                .map(shift -> {
                    shift.setName(shiftDto.getName());
                    shift.setStartTime(shiftDto.getStartTime());
                    shift.setEndTime(shiftDto.getEndTime());
                    
                    // Сначала удаляем смену из всех старых групп
                    if (shift.getGroups() != null) {
                        for (Group oldGroup : shift.getGroups()) {
                            oldGroup.getShifts().remove(shift);
                            groupRepository.save(oldGroup);
                        }
                    }
                    
                    // Обновляем группы
                    if (shiftDto.getGroupIds() != null && !shiftDto.getGroupIds().isEmpty()) {
                        List<Group> newGroups = groupRepository.findAllById(shiftDto.getGroupIds());
                        
                        // Проверяем, что все запрошенные группы найдены
                        if (newGroups.size() != shiftDto.getGroupIds().size()) {
                            System.out.println("Warning: Not all groups found. Requested: " + 
                                shiftDto.getGroupIds() + ", Found: " + newGroups.size());
                        }
                        
                        // Устанавливаем новые группы для смены
                        shift.setGroups(newGroups);
                        
                        // Добавляем смену в новые группы (синхронизируем двунаправленную связь)
                        for (Group newGroup : newGroups) {
                            if (newGroup.getShifts() == null) {
                                newGroup.setShifts(new java.util.HashSet<>());
                            }
                            newGroup.getShifts().add(shift);
                            groupRepository.save(newGroup);
                        }
                    } else {
                        shift.setGroups(java.util.Collections.emptyList());
                    }
                    
                    Shift savedShift = shiftRepository.save(shift);
                    return convertToDto(savedShift);
                });
    }
    
    public boolean deleteShift(Long id) {
        Optional<Shift> shift = shiftRepository.findById(id);
        if (shift.isPresent()) {
            shiftRepository.delete(shift.get());
            return true;
        }
        return false;
    }
    
    public List<ShiftDto> searchShifts(String query) {
        return shiftRepository.searchShifts(query).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    private ShiftDto convertToDto(Shift shift) {
        ShiftDto dto = new ShiftDto();
        dto.setId(shift.getId());
        dto.setName(shift.getName());
        dto.setStartTime(shift.getStartTime());
        dto.setEndTime(shift.getEndTime());
        
        if (shift.getGroups() != null) {
            dto.setGroupIds(shift.getGroups().stream()
                    .map(Group::getId)
                    .collect(Collectors.toSet()));
            dto.setGroupNames(shift.getGroups().stream()
                    .map(Group::getName)
                    .collect(Collectors.toSet()));
        }
        
        return dto;
    }
} 