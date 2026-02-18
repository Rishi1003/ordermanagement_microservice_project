package com.example.apigateway.filter;

import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

        public static final List<String> openApiEndpoints = List.of(
                        "/api/v1/auth/register",
                        "/api/v1/auth/login",
                        "/eureka");

        public Predicate<ServerHttpRequest> isSecured = request -> {
                // Allow POST /api/v1/users for public registration
                if (request.getURI().getPath().equals("/api/v1/users") && request.getMethod() == HttpMethod.POST) {
                        return false;
                }

                // Standard check for other open endpoints
                return openApiEndpoints
                                .stream()
                                .noneMatch(uri -> request.getURI().getPath().contains(uri));
        };

}
