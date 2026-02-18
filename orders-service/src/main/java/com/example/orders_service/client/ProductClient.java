package com.example.orders_service.client;

import com.example.orders_service.dto.ProductResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Service
public class ProductClient {

    private final WebClient.Builder webClientBuilder;

    public ProductClient(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @CircuitBreaker(name = "product", fallbackMethod = "fallbackProduct")
    public ProductResponse getProductById(UUID productId) {
        return webClientBuilder.build().get()
                .uri("lb://product-service/api/v1/products/" + productId)
                .retrieve()
                .bodyToMono(ProductResponse.class)
                .block();
    }

    public ProductResponse fallbackProduct(UUID productId, Throwable t) {
        throw new RuntimeException("Product Service is down or product not found: " + t.getMessage());
    }
}
