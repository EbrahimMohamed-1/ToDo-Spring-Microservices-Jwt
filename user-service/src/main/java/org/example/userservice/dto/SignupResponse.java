package org.example.userservice.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class SignupResponse {
    String message;
    private LocalDateTime timestamp;
    public SignupResponse(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
