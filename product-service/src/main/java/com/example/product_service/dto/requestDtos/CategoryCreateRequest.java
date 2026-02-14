package com.example.product_service.dto.requestDtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryCreateRequest(
    @NotBlank String name,
    @NotBlank @Size(min = 10, max = 255) String description
) {
    
}
