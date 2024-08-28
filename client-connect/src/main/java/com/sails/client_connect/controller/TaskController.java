package com.sails.client_connect.controller;

import com.sails.client_connect.dto.TaskDTO;
import com.sails.client_connect.entity.Priority;
import com.sails.client_connect.exception.UserNotFoundException;
import com.sails.client_connect.response.ApiResponse;
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
public ResponseEntity<ApiResponse<List<TaskDTO>>> getAllTasks(
        HttpSession session,
        @RequestParam(required = false) String sortBy,
        @RequestParam(required = false) String filterBy,
        @RequestParam(required = false) String filterValue) {
    Long userId = (Long) session.getAttribute("userId");
    if (userId == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>("User not authorized", HttpStatus.UNAUTHORIZED, null, null));
    }
    List<TaskDTO> tasks = taskService.getAllTasks(userId, sortBy, filterBy, filterValue);
    return ResponseEntity.ok(new ApiResponse<>("Tasks retrieved successfully", HttpStatus.OK, tasks, null));
}

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskDTO>> getTaskById(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>("User not authorized", HttpStatus.UNAUTHORIZED, null, null));
        }
        try {
            TaskDTO taskDTO = taskService.getTaskById(id, userId);
            return ResponseEntity.ok(new ApiResponse<>("Task retrieved successfully", HttpStatus.OK, taskDTO, null));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Task not found or not authorized", HttpStatus.NOT_FOUND, null, null));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<TaskDTO>> createTask(@Valid @RequestBody TaskDTO taskDTO, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>("User not authorized", HttpStatus.UNAUTHORIZED, null, null));
        }
        taskDTO.setUserId(userId);
        TaskDTO createdTask = taskService.createTask(taskDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Task created successfully", HttpStatus.CREATED, createdTask, null));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<ApiResponse<TaskDTO>> patchUpdateTask(@PathVariable Long id, @RequestBody TaskDTO taskDTO, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>("User not authorized", HttpStatus.UNAUTHORIZED, null, null));
        }
        taskDTO.setUserId(userId);
        TaskDTO updatedTask = taskService.patchUpdateTask(id, taskDTO);
        return ResponseEntity.ok(new ApiResponse<>("Task updated successfully", HttpStatus.OK, updatedTask, null));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteTask(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>("User not authorized", HttpStatus.UNAUTHORIZED, null, null));
        }
        try {
            taskService.deleteTask(id, userId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiResponse<>("Task deleted successfully", HttpStatus.NO_CONTENT, null, null));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Task not found or not authorized", HttpStatus.NOT_FOUND, null, null));
        }
    }


}
