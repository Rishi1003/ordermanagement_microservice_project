package com.example.apigateway.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> map = super.getErrorAttributes(request, options);
        Throwable error = getError(request);

        map.put("timestamp", OffsetDateTime.now());
        map.put("path", request.path());

        if (error instanceof ResponseStatusException) {
            ResponseStatusException ex = (ResponseStatusException) error;
            map.put("status", ex.getStatusCode().value());
            map.put("error", ((HttpStatus) ex.getStatusCode()).getReasonPhrase());
            map.put("message", ex.getMessage());
        } else if (error instanceof ExpiredJwtException || error instanceof SignatureException) {
            map.put("status", HttpStatus.UNAUTHORIZED.value());
            map.put("error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            map.put("message", "Invalid or expired token");
        } else {
            // Handle @ResponseStatus annotation if present
            MergedAnnotation<ResponseStatus> responseStatusAnnotation = MergedAnnotations
                    .from(error.getClass(), MergedAnnotations.SearchStrategy.TYPE_HIERARCHY).get(ResponseStatus.class);

            if (responseStatusAnnotation.isPresent()) {
                HttpStatus status = responseStatusAnnotation.getValue("code", HttpStatus.class)
                        .orElse(HttpStatus.INTERNAL_SERVER_ERROR);
                map.put("status", status.value());
                map.put("error", status.getReasonPhrase());
                map.put("message", error.getMessage());
            } else {
                map.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                map.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
                map.put("message", error.getMessage()); // In production, might want to hide distinct message
            }
        }

        // Remove standard Spring Boot attributes we don't want
        map.remove("requestId");

        return map;
    }
}
