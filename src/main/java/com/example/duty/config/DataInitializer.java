package com.example.duty.config;

import com.example.duty.model.User;
import com.example.duty.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private final UserRepository userRepository;
    
    @Autowired
    public DataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public void run(String... args) throws Exception {
        // Создаем тестовые данные только если база пустая
        if (userRepository.count() == 0) {
            createSampleUsers();
        }
    }
    
    private void createSampleUsers() {
        User user1 = new User("Иван", "Иванов", "Иванович ", 
                LocalDate.of(1990, 5, 15), "Разработчик");
        userRepository.save(user1);
        
        User user2 = new User("Мария", "Петрова", "Сергеевна", 
                LocalDate.of(1985, 8, 22), "Менеджер");
        userRepository.save(user2);
        
        User user3 = new User("Алексей", "Сидоров", "Петрович", 
                LocalDate.of(1992, 3, 10), "Тестировщик");
        userRepository.save(user3);
        
        User user4 = new User("Елена", "Козлова", "Александровна", 
                LocalDate.of(1988, 12, 5), "Дизайнер");
        userRepository.save(user4);
        
        User user5 = new User("Дмитрий", "Смирнов", "Владимирович", 
                LocalDate.of(1995, 7, 18), "Аналитик");
        userRepository.save(user5);
    }
} 