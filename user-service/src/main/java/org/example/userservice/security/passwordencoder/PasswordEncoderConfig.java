package org.example.userservice.security.passwordencoder;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderConfig {
    @Bean
    PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();

    }
}
