package com.example.miniproject.config;

import com.example.miniproject.enums.Role;
import com.example.miniproject.model.entity.User;
import com.example.miniproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail("admin@example.com").isEmpty()) {
            User admin = User.builder()
                    .fullName("Admin")
                    .email("admin@example.com")
                    .password("admin123") // In real app, hash
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);
        }
    }
}
