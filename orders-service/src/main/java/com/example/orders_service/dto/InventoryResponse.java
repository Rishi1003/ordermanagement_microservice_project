package com.example.orders_service.dto;

import java.time.Instant;
import java.util.UUID;

public record InventoryResponse(
    UUID id,
    UUID productId,
    Integer availableQuantity,
    Integer reservedQuantity,
    Instant updatedAt
) {}
