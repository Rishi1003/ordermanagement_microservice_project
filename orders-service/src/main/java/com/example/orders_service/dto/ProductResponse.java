package com.example.orders_service.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        String status,
        UUID categoryId,
        String categoryName,
        String sku,
        Instant createdAt,
        Instant updatedAt
) {}
