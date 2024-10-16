package com.app_delivery_test1.backend.config; // ajuste o pacote conforme necessário

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Permite requisições a todos os endpoints começando com /api/
                .allowedOrigins("http://localhost:4200") // URL do seu frontend Angular
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
}
