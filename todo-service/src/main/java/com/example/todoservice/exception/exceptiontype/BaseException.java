package com.example.todoservice.exception.exceptiontype;

import com.example.todoservice.exception.error.Errors;

import java.text.MessageFormat;

public class BaseException extends RuntimeException {

    private final Errors error;
    private final Object[] args;

    public BaseException(Errors error) {
        super(error.getFullMessage());
        this.error = error;
        this.args = new Object[]{};
    }

    public BaseException(Errors error, Object... args) {
        super(MessageFormat.format(error.getFullMessage(), args));
        this.error = error;
        this.args = args;
    }

    public BaseException(Errors error, Throwable cause, Object... args) {
        super(MessageFormat.format(error.getFullMessage(), args), cause);
        this.error = error;
        this.args = args;
    }
}
