package com.example.orders_service.client;

import com.example.orders_service.dto.InventoryCreateUpdateRequest;
import com.example.orders_service.dto.InventoryResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Service
public class InventoryClient {

    private final WebClient.Builder webClientBuilder;

    public InventoryClient(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackInventoryCheck")
    public InventoryResponse getInventoryByProductId(UUID productId) {
        return webClientBuilder.build().get()
                .uri("lb://inventory-service/api/v1/inventory/product/" + productId)
                .header("X-User-Role", "ADMIN") // Accessing secured endpoint
                .retrieve()
                .bodyToMono(InventoryResponse.class)
                .block();
    }
    
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackInventoryUpdate")
    public InventoryResponse updateInventory(UUID inventoryId, InventoryCreateUpdateRequest request) {
        return webClientBuilder.build().put()
                .uri("lb://inventory-service/api/v1/inventory/" + inventoryId)
                .header("X-User-Role", "ADMIN")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(InventoryResponse.class)
                .block();
    }

    public InventoryResponse fallbackInventoryCheck(UUID productId, Throwable t) {
        throw new RuntimeException("Inventory Service is unavailable or inventory not found: " + t.getMessage());
    }
    
    public InventoryResponse fallbackInventoryUpdate(UUID inventoryId, InventoryCreateUpdateRequest request, Throwable t) {
        throw new RuntimeException("Failed to update inventory: " + t.getMessage());
    }
}
