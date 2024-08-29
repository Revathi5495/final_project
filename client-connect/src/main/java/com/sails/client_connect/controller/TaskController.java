package com.sails.client_connect.controller;

import com.sails.client_connect.dto.TaskDTO;
import com.sails.client_connect.exception.UserNotFoundException;
import com.sails.client_connect.response.ApiResponse;
import com.sails.client_connect.service.TaskService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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
            @RequestParam(required = false) String filterByValue) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>("User not authorized", HttpStatus.UNAUTHORIZED, null));
        }
        List<TaskDTO> tasks = taskService.getAllTasks(userId, sortBy, filterBy, filterByValue);
        return ResponseEntity.ok(new ApiResponse<>("Tasks retrieved successfully", HttpStatus.OK, tasks));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskDTO>> getTaskById(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>("User not authorized", HttpStatus.UNAUTHORIZED, null));
        }
        try {
            TaskDTO taskDTO = taskService.getTaskById(id, userId);
            return ResponseEntity.ok(new ApiResponse<>("Task retrieved successfully", HttpStatus.OK, taskDTO));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Task not found or not authorized", HttpStatus.NOT_FOUND, null));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<TaskDTO>> createTask(@Valid @RequestBody TaskDTO taskDTO, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>("User not authorized", HttpStatus.UNAUTHORIZED, null));
        }
        taskDTO.setUserId(userId);
        TaskDTO createdTask = taskService.createTask(taskDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Task created successfully", HttpStatus.CREATED, createdTask));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<ApiResponse<TaskDTO>> patchUpdateTask(@PathVariable Long id, @RequestBody TaskDTO taskDTO, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>("User not authorized", HttpStatus.UNAUTHORIZED, null));
        }
        taskDTO.setUserId(userId);
        TaskDTO updatedTask = taskService.patchUpdateTask(id, taskDTO);
        return ResponseEntity.ok(new ApiResponse<>("Task updated successfully", HttpStatus.OK, updatedTask));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteTask(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>("User not authorized", HttpStatus.UNAUTHORIZED, null));
        }
        try {
            taskService.deleteTask(id, userId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse<>("Task deleted successfully", HttpStatus.OK, null));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Task not found or not authorized", HttpStatus.NOT_FOUND, null));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<TaskDTO>> searchTasks(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        Page<TaskDTO> taskPage = taskService.searchTasks(query, page, size, userId);
        return ResponseEntity.ok(taskPage);
    }

    @GetMapping("/filter-sort")
    public ResponseEntity<Page<TaskDTO>> filterAndSortTasks(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String clientName,
            @RequestParam(required = false) String taskTitle,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) LocalDateTime dueDateTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "taskTitle") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir, HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Page<TaskDTO> taskPage = taskService.filterAndSortTasks(
                id, clientName, taskTitle, status, priority, dueDateTime, page, size, sort, userId);
        return ResponseEntity.ok(taskPage);
    }


}
