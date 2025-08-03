package com.example.duty.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {
    
    // Обработка ошибок - возвращаем главную страницу
    @GetMapping("/error")
    public String error() {
        return "forward:/index.html";
    }
} 