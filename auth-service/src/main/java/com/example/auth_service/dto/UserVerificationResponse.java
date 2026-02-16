package com.example.auth_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVerificationResponse {
    @JsonProperty("isValid")
    private boolean isValid;
    private String message;
    private String token;
    private String role;
}
