package org.example.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseInfo {
    private int id;
    private String email;
    private boolean active;
}
