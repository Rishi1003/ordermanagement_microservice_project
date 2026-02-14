package com.example.product_service.dto.responseDtos;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.example.product_service.entity.ProductStatus;

public record ProductResponse(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        ProductStatus status,
        UUID categoryId,
        String categoryName,
        String sku,
        Instant createdAt,
        Instant updatedAt
) {}

