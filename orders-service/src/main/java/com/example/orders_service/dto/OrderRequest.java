package com.example.orders_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

public record OrderRequest(
    UUID userId,
    @NotEmpty(message = "Order line items cannot be empty") @Valid List<OrderLineItemsDto> orderLineItems
) {}
