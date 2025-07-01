package org.example.userservice.exception.exceptiontype;


public class MissingTokenException extends RuntimeException {
    public MissingTokenException(String message) {
        super(message);
    }
}

