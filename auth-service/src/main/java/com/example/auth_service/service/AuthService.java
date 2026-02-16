package com.example.auth_service.service;

import com.example.auth_service.client.UserClient;
import com.example.auth_service.dto.AuthRequest;
import com.example.auth_service.dto.AuthResponse;
import com.example.auth_service.util.JwtUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import com.example.auth_service.exception.UserNotFoundException;
import com.example.auth_service.exception.InvalidCredentialsException;

@Service
public class AuthService {

    private final UserClient userClient;
    private final JwtUtil jwtUtil;

    public AuthService(UserClient userClient, JwtUtil jwtUtil) {
        this.userClient = userClient;
        this.jwtUtil = jwtUtil;
    }

    public Mono<AuthResponse> login(AuthRequest request) {
        return userClient.verifyUser(request)
                .map(response -> {
                    System.out.println("Response: " + response);
                    if (response.isValid()) {
                        String token = jwtUtil.generateToken(request.getEmail(), response.getRole());
                        return AuthResponse.builder()
                                .token(token)
                                .role(response.getRole())
                                .build();
                    } else {
                        throw new InvalidCredentialsException("Invalid credentials");
                    }
                })
                .onErrorResume(e -> {
                    if (e instanceof org.springframework.web.reactive.function.client.WebClientResponseException.NotFound) {
                        return Mono.error(new UserNotFoundException("User not found"));
                    }
                    return Mono.error(e);
                });
    }
}
