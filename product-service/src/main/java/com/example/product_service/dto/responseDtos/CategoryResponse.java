package com.example.product_service.dto.responseDtos;

import java.util.UUID;

public record CategoryResponse(
    UUID categoryId,
    String name,
    String description
) {
}
