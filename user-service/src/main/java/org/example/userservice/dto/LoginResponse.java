package org.example.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor

public class LoginResponse {
    private String email;
    private String accessToken;
    private String refreshToken;
}
