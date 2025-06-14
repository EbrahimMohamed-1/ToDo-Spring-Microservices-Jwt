package org.example.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class ResetPasswordRequest {
    @NotBlank(message = "user name is required")
    private String email;
}
