package com.example.product_service.dto.requestDtos;

import java.util.UUID;

public record CategoryUpdateRequest(UUID categoryId, String name, String description) {
    
}
