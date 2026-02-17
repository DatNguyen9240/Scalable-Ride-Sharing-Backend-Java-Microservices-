package com.ridesharing.userservice.service;

import com.ridesharing.userservice.model.User;
import com.ridesharing.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        if (user.getTenantId() == null || user.getTenantId().isBlank()) {
            throw new RuntimeException("tenantId is required");
        }
        if (userRepository.existsByUsernameAndTenantId(user.getUsername(), user.getTenantId())) {
            throw new RuntimeException("Username already exists for this tenant");
        }
        if (userRepository.existsByEmailAndTenantId(user.getEmail(), user.getTenantId())) {
            throw new RuntimeException("Email already exists for this tenant");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}