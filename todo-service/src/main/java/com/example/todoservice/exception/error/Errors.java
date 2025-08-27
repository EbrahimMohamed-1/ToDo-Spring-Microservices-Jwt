package com.example.todoservice.exception.error;

public interface Errors {
    String INVALID_REQUEST = "Invalid Request";
    String TASK_NOT_FOUND = "Task Not Found";
    String TASK_ALREADY_EXIST = "Task Already Exist";
    String TASK_NOT_DELETED = "Task Not Deleted";
    String TASK_NOT_UPDATED = "Task Not Updated";
    String TASK_NOT_CREATED = "Task Not Created";
    String USER_NOT_FOUND = "User Not Found";
    String USER_ALREADY_EXIST = "User Already Exist";
    String USER_NOT_DELETED = "User Not Deleted";
    String USER_NOT_UPDATED = "User Not Updated";

    Domains domain();

    String code();

    String defaultMessage();

    String name();

    default String label() {
        return this.name();
    }


    default String getFullMessage() {
        return "[" + domain() + "-" + code() + "] [" + label() + "]: " + defaultMessage();
    }
}
