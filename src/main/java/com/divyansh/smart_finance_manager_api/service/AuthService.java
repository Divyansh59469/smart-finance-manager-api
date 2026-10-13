package com.divyansh.smart_finance_manager_api.service;

import com.divyansh.smart_finance_manager_api.dto.AuthResponse;
import com.divyansh.smart_finance_manager_api.dto.LoginRequest;
import com.divyansh.smart_finance_manager_api.dto.RegisterRequest;
import com.divyansh.smart_finance_manager_api.model.User;
import com.divyansh.smart_finance_manager_api.repository.UserRepository;
import com.divyansh.smart_finance_manager_api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }

        User user = new User();
        user.setName(request.name().trim());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);

        return new AuthResponse(jwtService.generateToken(email), user.getName(), email);
    }

    public AuthResponse login(LoginRequest request) {
        String email = request.email().trim().toLowerCase();

        User user = userRepository.findByEmail(email)
                .filter(u -> passwordEncoder.matches(request.password(), u.getPassword()))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Invalid email or password"));

        return new AuthResponse(jwtService.generateToken(email), user.getName(), email);
    }
}