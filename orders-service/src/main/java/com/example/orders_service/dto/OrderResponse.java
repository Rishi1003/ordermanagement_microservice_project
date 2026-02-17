package com.example.orders_service.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
    UUID id,
    String orderNumber,
    UUID userId,
    List<OrderLineItemsDto> orderLineItems,
    Instant createdAt
) {}
