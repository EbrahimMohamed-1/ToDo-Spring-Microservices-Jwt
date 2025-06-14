package org.example.userservice.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String email;
    private String token;
}
