package com.example.product_service.dto.requestDtos;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductCreateUpdateRequest(
    @NotBlank String name, 
    @Size(max = 1000) String description, 
    @NotNull  @DecimalMin(value = "0.0", inclusive = false) BigDecimal price, 
    @NotNull UUID categoryId, 
    @NotNull String sku ) {
    
}
