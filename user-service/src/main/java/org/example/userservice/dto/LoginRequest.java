package org.example.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.validation.annotation.Validated;


@Data
@Validated
public class LoginRequest {

    @NotBlank(message = "user name is required")
    private String email;

    @NotBlank(message = "password is required")
    private String password;

}
