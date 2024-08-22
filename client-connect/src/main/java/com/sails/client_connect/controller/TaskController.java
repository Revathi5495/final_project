package com.sails.client_connect.controller;

import com.sails.client_connect.dto.TaskDTO;
import com.sails.client_connect.entity.Task;
import com.sails.client_connect.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/all-tasks")
    public List<TaskDTO> getAllTasks() {
        return taskService.getAllTasks();
    }
}
