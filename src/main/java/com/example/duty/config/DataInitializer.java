package com.example.duty.config;

import com.example.duty.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Проверяем, есть ли уже данные в базе
        if (userRepository.count() > 0) {
            System.out.println("База данных уже содержит данные. Инициализация пропущена.");
            return;
        }
        
        System.out.println("База данных пуста. Готово к использованию.");
        System.out.println("Добавьте пользователей, группы, смены и города через веб-интерфейс.");
    }
} 