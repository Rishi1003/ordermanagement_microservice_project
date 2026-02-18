package com.example.orders_service.client;

import com.example.orders_service.dto.UserResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class UserClient {

    private final WebClient.Builder webClientBuilder;

    public UserClient(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @CircuitBreaker(name = "user", fallbackMethod = "fallbackUser")
    public UserResponse getUserByEmail(String email) {
        return webClientBuilder.build().get()
                .uri("lb://user-service/api/v1/users/email/" + email)
                .header("X-User-Role", "ADMIN") // Assuming internal call needs privileges or just authenticated
                .retrieve()
                .bodyToMono(UserResponse.class)
                .block();
    }

    public UserResponse fallbackUser(String email, Throwable t) {
        throw new RuntimeException("User Service is unavailable or user not found: " + t.getMessage());
    }
}
