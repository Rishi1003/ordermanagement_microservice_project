package com.example.user_service.dto.responseDtos;

public record UserVerificationResponse(
        boolean isValid,
        String message,
        String token, // Optional, if needed for future use
        String role) {
}
