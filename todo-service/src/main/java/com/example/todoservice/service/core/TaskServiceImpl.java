package com.example.todoservice.service.core;

import com.example.todoservice.repository.TaskRepository;
import com.example.todoservice.service.api.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;


    @Override
    public void saveTask() {

    }

    @Override
    public void updateTask() {

    }

    @Override
    public void deleteTask() {

    }

    @Override
    public void getTaskById() {

    }

    @Override
    public void getAllTasks() {

    }

    @Override
    public void getTasksByUserId() {

    }
}
