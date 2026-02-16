package com.example.auth_service.client;

import com.example.auth_service.dto.AuthRequest;
import com.example.auth_service.dto.UserVerificationResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class UserClient {

    private final WebClient.Builder webClientBuilder;

    public UserClient(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<UserVerificationResponse> verifyUser(AuthRequest request) {
        return webClientBuilder.build()
                .post()
                .uri("http://user-service/api/v1/users/verify")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UserVerificationResponse.class);
    }
}
