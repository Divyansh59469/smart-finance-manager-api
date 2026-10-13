package com.divyansh.smart_finance_manager_api.dto;

public record AuthResponse(
        String token,
        String name,
        String email
) {
}