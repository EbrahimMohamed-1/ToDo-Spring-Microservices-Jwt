package org.example.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class ResetPasswordRequest {
    @NotBlank(message = "user name is required")
    @Email(message = "invalid email")
    private String email;
}
