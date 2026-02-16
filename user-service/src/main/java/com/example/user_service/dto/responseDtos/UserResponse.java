package com.example.user_service.dto.responseDtos;

import com.example.user_service.entity.Role;
import java.time.Instant;
import java.util.UUID;

public record UserResponse(
    UUID id,
    String username,
    String email,
    Role role,
    Instant createdAt,
    Instant updatedAt
) {}
