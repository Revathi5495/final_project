package com.sails.client_connect.controller;

import com.sails.client_connect.dto.TaskDTO;
import com.sails.client_connect.entity.Priority;
import com.sails.client_connect.exception.UserNotFoundException;
import com.sails.client_connect.service.TaskService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String filterBy,
            @RequestParam(required = false) String filterValue) {
        return ResponseEntity.ok(taskService.getAllTasks(sortBy, filterBy, filterValue));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        TaskDTO taskDTO = taskService.getTaskById(id);
        return ResponseEntity.ok(taskDTO);
    }


    @PostMapping("/create")
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO taskDTO, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        taskDTO.setAssignedToId(userId);
        TaskDTO createdTask = taskService.createTask(taskDTO);
        return ResponseEntity.ok(createdTask);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<TaskDTO> patchUpdateTask(@PathVariable Long id,@RequestBody TaskDTO taskDTO,HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            taskDTO.setAssignedToId(userId);
        }
        TaskDTO updatedTask = taskService.patchUpdateTask(id, taskDTO);
        return ResponseEntity.ok(updatedTask);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) throws UserNotFoundException {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
    //indira
//    @GetMapping("/sortedByPriority")
//    public List<TaskDTO> getTasksSortedByPriority(@RequestParam(required = false) String sortBy) {
//        return taskService.getTasksSortedByPriority(sortBy);
//    }
//    @GetMapping("/sortedByDueDate")
//    public List<TaskDTO> getTasksSortedByDueDate(@RequestParam(required = false) String sortBy) {
//        return taskService.getTasksSortedByDueDate(sortBy);
//    }
//    @GetMapping("/sortedByStatus")
//    public List<TaskDTO> getTasksSortedByStatus(@RequestParam(required = false) String sortBy) {
//        return taskService.getTasksSortedByStatus(sortBy);
//    }
//    @GetMapping("/filterByPriority")
//    public List<TaskDTO> getTasksByPriority(@RequestParam Priority priority) {
//        return taskService.getTasksByPriority(priority);
//    }
}
