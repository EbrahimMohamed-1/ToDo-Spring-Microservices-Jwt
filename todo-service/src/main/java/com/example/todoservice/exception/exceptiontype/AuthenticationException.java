package com.example.todoservice.exception.exceptiontype;

import com.example.todoservice.exception.error.Errors;

public class AuthenticationException extends BaseException{

    public AuthenticationException(Errors error) {
        super(error);
    }

    public AuthenticationException(Errors error, Object... args) {
        super(error, args);
    }

    public AuthenticationException(Errors error, Throwable cause, Object... args) {
        super(error, cause, args);
    }
}
