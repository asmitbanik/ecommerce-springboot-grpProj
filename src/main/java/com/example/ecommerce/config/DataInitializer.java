package com.example.ecommerce.config;

import com.example.ecommerce.model.Role;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner createAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String adminUsername = System.getenv().getOrDefault("ADMIN_USERNAME", "admin");
            String adminPassword = System.getenv().getOrDefault("ADMIN_PASSWORD", "admin123");
            String adminEmail = System.getenv().getOrDefault("ADMIN_EMAIL", "admin@example.com");
            if (!userRepository.existsByUsername(adminUsername)) {
                User admin = new User();
                admin.setUsername(adminUsername);
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode(adminPassword));
                admin.setRoles(Set.of(Role.ROLE_ADMIN, Role.ROLE_USER));
                userRepository.save(admin);
            }
        };
    }
}