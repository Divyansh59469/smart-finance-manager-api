package com.divyansh.smart_finance_manager_api.controller;

import com.divyansh.smart_finance_manager_api.dto.AuthResponse;
import com.divyansh.smart_finance_manager_api.dto.LoginRequest;
import com.divyansh.smart_finance_manager_api.dto.RegisterRequest;
import com.divyansh.smart_finance_manager_api.dto.UserResponse;
import com.divyansh.smart_finance_manager_api.model.User;
import com.divyansh.smart_finance_manager_api.security.CookieService;
import com.divyansh.smart_finance_manager_api.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CookieService cookieService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(
            @Valid @RequestBody RegisterRequest request,
            HttpServletResponse response
    ) {
        AuthResponse auth = authService.register(request);
        cookieService.addTokenCookie(response, auth.token());
        return new UserResponse(auth.name(), auth.email());
    }

    @PostMapping("/login")
    public UserResponse login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        AuthResponse auth = authService.login(request);
        cookieService.addTokenCookie(response, auth.token());
        return new UserResponse(auth.name(), auth.email());
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletResponse response) {
        cookieService.clearTokenCookie(response);
    }

    @GetMapping("/me")
    public UserResponse me(@AuthenticationPrincipal User user) {
        return new UserResponse(user.getName(), user.getEmail());
    }
}