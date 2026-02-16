package com.example.user_service.dto.requestDtos;

import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(
        @NotBlank(message = "Email is required") String email, // Can be email or username
        @NotBlank(message = "Password is required") String password) {
}
