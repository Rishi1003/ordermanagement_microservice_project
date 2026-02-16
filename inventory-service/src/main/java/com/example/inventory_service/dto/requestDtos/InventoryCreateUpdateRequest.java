package com.example.inventory_service.dto.requestDtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record InventoryCreateUpdateRequest(
    @NotNull UUID productId,
    @NotNull @Min(0) Integer availableQuantity,
    @NotNull @Min(0) Integer reservedQuantity
) {}
