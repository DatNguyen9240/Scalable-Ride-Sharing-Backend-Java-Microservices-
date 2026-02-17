package com.ridesharing.userservice.controller;

import com.ridesharing.userservice.model.User;
import com.ridesharing.userservice.service.UserService;
import com.ridesharing.userservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            // tenantId should be provided in payload for tenant-scoped registration
            if (user.getTenantId() == null || user.getTenantId().isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("error", "tenantId is required"));
            }

            User registeredUser = userService.registerUser(user);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("userId", registeredUser.getId());

            // return a token including tenant claim
            String token = jwtUtil.generateToken(registeredUser.getUsername(), registeredUser.getTenantId());
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        try {
            String username = loginRequest.get("username");
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    username,
                    loginRequest.get("password")
                )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // include tenant claim in token
            String tenantId = userService.findByUsername(username)
                    .map(User::getTenantId)
                    .orElse("default");

            String token = jwtUtil.generateToken(username, tenantId);
            return ResponseEntity.ok(Map.of("token", token));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid credentials"));
        }
    }
}