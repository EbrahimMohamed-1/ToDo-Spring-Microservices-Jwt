package com.example.todoservice.exception.exceptiontype;

import com.example.todoservice.exception.error.Errors;

public class BusinessException extends BaseException{

  public BusinessException(Errors error) {
    super(error);
  }

  public BusinessException(Errors error, Object... args) {
    super(error, args);
  }

  public BusinessException(Errors error, Throwable cause, Object... args) {
    super(error, cause, args);
  }
}
