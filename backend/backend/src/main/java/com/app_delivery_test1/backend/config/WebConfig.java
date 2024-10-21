package com.app_delivery_test1.backend.config; // ajuste o pacote conforme necessário

import jakarta.annotation.Nullable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/pratos/**")
                        .allowedOrigins("http://localhost:4200") // Endereço do seu frontend Angular
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true); // Permitir credenciais (cookies, autenticações, etc)

            }

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/imagens/**")
                        .addResourceLocations("file:" + System.getProperty("user.dir") + "/src/main/resources/static/imagens/");
            }
        };
    }
}
