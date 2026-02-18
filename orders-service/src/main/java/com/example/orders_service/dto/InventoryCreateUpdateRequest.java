package com.example.orders_service.dto;

import java.util.UUID;

public record InventoryCreateUpdateRequest(
    UUID productId,
    Integer availableQuantity,
    Integer reservedQuantity
) {}
