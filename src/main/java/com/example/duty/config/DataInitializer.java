package com.example.duty.config;

import com.example.duty.model.User;
import com.example.duty.model.Group;
import com.example.duty.model.Shift;
import com.example.duty.model.City;
import com.example.duty.repository.UserRepository;
import com.example.duty.repository.GroupRepository;
import com.example.duty.repository.ShiftRepository;
import com.example.duty.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private GroupRepository groupRepository;
    
    @Autowired
    private ShiftRepository shiftRepository;
    
    @Autowired
    private CityRepository cityRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Создаем тестовые смены
        Shift morningShift = new Shift("Утренняя смена", LocalTime.of(8, 0), LocalTime.of(16, 0));
        Shift eveningShift = new Shift("Вечерняя смена", LocalTime.of(16, 0), LocalTime.of(0, 0));
        Shift nightShift = new Shift("Ночная смена", LocalTime.of(0, 0), LocalTime.of(8, 0));
        
        shiftRepository.saveAll(Arrays.asList(morningShift, eveningShift, nightShift));
        
        // Создаем города
        City moscow = new City("Москва", "GMT+3");
        City spb = new City("Санкт-Петербург", "GMT+3");
        City ekb = new City("Екатеринбург", "GMT+5");
        City novosibirsk = new City("Новосибирск", "GMT+7");
        
        cityRepository.saveAll(Arrays.asList(moscow, spb, ekb, novosibirsk));
        
        // Создаем тестовые группы
        Group developers = new Group("Разработчики");
        Group managers = new Group("Менеджеры");
        Group designers = new Group("Дизайнеры");
        
        // Назначаем смены группам
        developers.setShifts(new HashSet<>(Arrays.asList(morningShift, eveningShift)));
        managers.setShifts(new HashSet<>(Arrays.asList(morningShift)));
        designers.setShifts(new HashSet<>(Arrays.asList(eveningShift, nightShift)));
        
        groupRepository.saveAll(Arrays.asList(developers, managers, designers));
        
        // Создаем тестовых пользователей
        User user1 = new User("Иван", "Иванов", "Иванович", LocalDate.of(1990, 5, 15), "Java Developer");
        user1.setGroup(developers);
        
        User user2 = new User("Петр", "Петров", "Петрович", LocalDate.of(1985, 8, 20), "Project Manager");
        user2.setGroup(managers);
        
        User user3 = new User("Анна", "Сидорова", "Александровна", LocalDate.of(1992, 3, 10), "UI/UX Designer");
        user3.setGroup(designers);
        
        User user4 = new User("Мария", "Козлова", "Сергеевна", LocalDate.of(1988, 12, 5), "Frontend Developer");
        user4.setGroup(developers);
        
        User user5 = new User("Алексей", "Смирнов", "Дмитриевич", LocalDate.of(1995, 7, 25), "Product Manager");
        user5.setGroup(managers);
        
        User user6 = new User("Елена", "Волкова", "Игоревна", LocalDate.of(1991, 9, 18), "Graphic Designer");
        user6.setGroup(designers);
        
        userRepository.saveAll(Arrays.asList(user1, user2, user3, user4, user5, user6));
    }
} 