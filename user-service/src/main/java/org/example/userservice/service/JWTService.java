package org.example.userservice.service;

import org.springframework.beans.factory.annotation.Value;

public class JWTService {

    @Value("${env_variables.JWT_SECRET}")
    public static String JWT_SECRET;

    @Value("${env_variables.JWT_EXPIRATION_MS}")
    public static String JWT_EXPIRATION_MS;



}
