package org.example.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.validation.annotation.Validated;


@Data
@Validated
public class LoginRequest {

    @NotBlank(message = "user name is required")
    @Email(message = "invalid email")
    private String email;

    @NotBlank(message = "password is required")
    private String password;

}
