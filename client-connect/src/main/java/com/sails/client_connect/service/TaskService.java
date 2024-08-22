package com.sails.client_connect.service;

import com.sails.client_connect.dto.TaskDTO;
import com.sails.client_connect.entity.Task;
import com.sails.client_connect.mapper.TaskMapper;
import com.sails.client_connect.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private TaskRepository taskRepository;

    private TaskMapper taskMapper;

    public List<TaskDTO> getAllTasks() {
        List<Task> taskEntities=taskRepository.findAll();
        return taskMapper.toDTOList(taskEntities);
    }
}
