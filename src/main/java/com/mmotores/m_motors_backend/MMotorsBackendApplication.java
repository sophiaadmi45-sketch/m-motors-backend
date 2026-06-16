package com.mmotores.m_motors_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class MMotorsBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MMotorsBackendApplication.class, args);
    }

    // Configuration CORS
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5173")  // Ton frontend Vite
                        .allowedMethods("*")
                        .allowedHeaders("*");
            }
        };
    }
}