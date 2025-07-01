package org.example.userservice.exception.exceptiontype;

public class EmailAlreadyFound extends RuntimeException {
    public EmailAlreadyFound(String message) {
        super(message);
    }
}
