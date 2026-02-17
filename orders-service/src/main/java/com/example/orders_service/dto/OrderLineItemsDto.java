package com.example.orders_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record OrderLineItemsDto(
    @NotBlank(message = "SKU code cannot be blank") String skuCode,
    @NotNull(message = "Price cannot be null") @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0") BigDecimal price,
    @NotNull(message = "Quantity cannot be null") @Min(value = 1, message = "Quantity must be at least 1") Integer quantity
) {}
