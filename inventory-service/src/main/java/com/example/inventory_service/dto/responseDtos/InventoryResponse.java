package com.example.inventory_service.dto.responseDtos;

import java.time.Instant;
import java.util.UUID;

public record InventoryResponse(
    UUID id,
    UUID productId,
    Integer availableQuantity,
    Integer reservedQuantity,
    Instant updatedAt
) {}
