package com.example.product_service.dto.requestDtos;

import java.util.UUID;

public record InventoryRequest(
        UUID productId,
        Integer availableQuantity,
        Integer reservedQuantity) {
}
