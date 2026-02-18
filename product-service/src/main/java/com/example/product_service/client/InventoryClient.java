package com.example.product_service.client;

import com.example.product_service.dto.requestDtos.InventoryRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class InventoryClient {

    private final WebClient.Builder webClientBuilder;

    public InventoryClient(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackInventory")
    public void createInventory(InventoryRequest request) {
        webClientBuilder.build().post()
                .uri("lb://inventory-service/api/v1/inventory")
                .header("X-User-Role", "ADMIN")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class)
                .block();

        log.info("Inventory created for product: {}", request.productId());
    }

    public void fallbackInventory(InventoryRequest request, Throwable t) {
        log.error("Failed to create inventory for product: {}. Reason: {}", request.productId(), t.getMessage());
        // In a real scenario, we might want to queue this or update a status.
        // For now, we just log the failure so the product creation isn't rolled back.
    }
}
