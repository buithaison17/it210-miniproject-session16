package com.example.miniproject.service;

import com.example.miniproject.enums.Role;
import com.example.miniproject.model.dto.UserDTO;
import com.example.miniproject.model.entity.User;
import com.example.miniproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public User register(UserDTO userDTO) {
        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            throw new RuntimeException("Mật khẩu xác nhận không khớp");
        }
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email đã tồn tại");
        }
        User user = User.builder()
                .fullName(userDTO.getFullName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword()) // In real app, hash password
                .role(Role.CUSTOMER)
                .build();
        return userRepository.save(user);
    }

    public Optional<User> login(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            return userOpt;
        }
        return Optional.empty();
    }
}
