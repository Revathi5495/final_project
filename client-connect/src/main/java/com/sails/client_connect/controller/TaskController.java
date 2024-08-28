package com.sails.client_connect.controller;

import com.sails.client_connect.dto.TaskDTO;
import com.sails.client_connect.entity.Priority;
import com.sails.client_connect.exception.UserNotFoundException;
import com.sails.client_connect.service.TaskService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user/tasks")
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks(
            HttpSession session,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String filterBy,
            @RequestParam(required = false) String filterValue) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<TaskDTO> tasks = taskService.getAllTasks(userId, sortBy, filterBy, filterValue);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        TaskDTO taskDTO = taskService.getTaskById(id, userId);
        return ResponseEntity.ok(taskDTO);
    }


    @PostMapping("/create")
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO taskDTO, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        taskDTO.setUserId(userId);
        TaskDTO createdTask = taskService.createTask(taskDTO);
        return ResponseEntity.ok(createdTask);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<TaskDTO> patchUpdateTask(@PathVariable Long id,@RequestBody TaskDTO taskDTO,HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        taskDTO.setUserId(userId);
        TaskDTO updatedTask = taskService.patchUpdateTask(id, taskDTO);
        return ResponseEntity.ok(updatedTask);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            taskService.deleteTask(id, userId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("Task with ID " + id + " has been successfully deleted.");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Task with ID " + id + " not found or you are not authorized to delete this task.");
        }
    }


}
