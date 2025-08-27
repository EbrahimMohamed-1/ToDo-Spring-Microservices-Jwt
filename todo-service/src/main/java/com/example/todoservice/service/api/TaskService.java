package com.example.todoservice.service.api;

public interface TaskService {

    void saveTask();
    void updateTask();
    void deleteTask();
    void getTaskById();
    void getAllTasks();
    void getTasksByUserId();
}
