package com.sails.client_connect.service;

import com.sails.client_connect.dto.TaskDTO;
import com.sails.client_connect.entity.*;
import com.sails.client_connect.exception.UserNotFoundException;
import com.sails.client_connect.mapper.TaskMapper;
import com.sails.client_connect.repository.CustomerRepository;
import com.sails.client_connect.repository.TaskRepository;
import com.sails.client_connect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    private final TaskMapper taskMapper;


    public TaskDTO getTaskById(Long id, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return taskRepository.findByIdAndUser(id, user)
                .map(taskMapper::toDTO)
                .orElseThrow(() -> new UserNotFoundException("Task not found with id: " + id));
    }

    public List<TaskDTO> getAllTasksToAdminView() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(taskMapper::toDTO)
                .collect(Collectors.toList());
    }


    public TaskDTO createTask(TaskDTO taskDTO) {

        User user = userRepository.findById(taskDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Customer customer = customerRepository.findById(taskDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        // Convert DTO to Entity and set additional fields
        Task task = taskMapper.toEntity(taskDTO);
        task.setUser(user);
        task.setCustomer(customer);
        task.setCreatedDate(LocalDateTime.now());
        task.setLastUpdatedDate(LocalDateTime.now());

        // Save the entity
        Task savedTask = taskRepository.save(task);

        // Convert saved entity to DTO and return
        return taskMapper.toDTO(savedTask);
    }

public TaskDTO patchUpdateTask(Long id, TaskDTO taskDTO) {
    User user = userRepository.findById(taskDTO.getUserId())
            .orElseThrow(() -> new UserNotFoundException("User not found"));
    Task task = taskRepository.findByIdAndUser(id, user)
            .orElseThrow(() -> new UserNotFoundException("Task not found with id: " + id));

    // Update only non-null fields
    if (taskDTO.getClientName() != null) {
        task.setClientName(taskDTO.getClientName());
    }
    if (taskDTO.getTaskTitle() != null) {
        task.setTaskTitle(taskDTO.getTaskTitle());
    }
    if (taskDTO.getDescription() != null) {
        task.setDescription(taskDTO.getDescription());
    }
    if (taskDTO.getDueDateTime() != null) {
        task.setDueDateTime(taskDTO.getDueDateTime());
    }
    if (taskDTO.getPriority() != null) {
        task.setPriority(taskDTO.getPriority());
    }
    if (taskDTO.getStatus() != null) {
        task.setStatus(taskDTO.getStatus());
    }
    if (taskDTO.getRecurrencePattern() != null) {
        try {
            task.setRecurrencePattern(RecurrencePattern.valueOf(taskDTO.getRecurrencePattern().name()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid recurrence pattern: " + taskDTO.getRecurrencePattern());
        }
    }
    if (taskDTO.getUserId() != null) {
        User assignedTo = userRepository.findById(taskDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + taskDTO.getUserId()));
        task.setUser(assignedTo);
    }
    if (taskDTO.getCustomerId() != null) {
        Customer customer = customerRepository.findById(taskDTO.getCustomerId())
                .orElseThrow(() -> new UserNotFoundException("Customer not found with id: " + taskDTO.getCustomerId()));
        task.setCustomer(customer);
    }

    // Update the lastUpdatedDate field to indicate a change
    task.setLastUpdatedDate(LocalDateTime.now());

    // Save the updated entity
    Task updatedTask = taskRepository.save(task);

    // Convert updated entity to DTO and return
    return taskMapper.toDTO(updatedTask);
}
    public void deleteTask(Long id, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Task task=taskRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new UserNotFoundException("Task not found with id: " + id)); // Throw exception if task not found
        taskRepository.delete(task);
    }

    public List<TaskDTO> getAllTasks(Long userId, String sortBy, String filterBy, String filterValue) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<Task> tasks = taskRepository.findByUser(user);

        // Apply filtering if filterBy and filterValue are provided
        if (filterBy != null && filterValue != null) {
            tasks = tasks.stream()
                    .filter(task -> filterTask(task, filterBy, filterValue))
                    .collect(Collectors.toList());
        }

        // Apply sorting based on the sortBy parameter
        tasks = applySorting(tasks, sortBy);

        return taskMapper.toDTOList(tasks);
    }

    private boolean filterTask(Task task, String filterBy, String filterValue) {
        switch (filterBy) {
            case "priority":
                return task.getPriority().name().equalsIgnoreCase(filterValue);
            case "status":
                return task.getStatus().name().equalsIgnoreCase(filterValue);
            case "dueDate":
                return task.getDueDateTime().toString().contains(filterValue);
            // Add more cases if necessary
            default:
                return true;
        }
    }

    private List<Task> applySorting(List<Task> tasks, String sortBy) {
        if ("priorityAsc".equals(sortBy)) {
            tasks.sort(Comparator.comparingInt(this::mapPriority));
        } else if ("priorityDesc".equals(sortBy)) {
            tasks.sort(Comparator.comparingInt(this::mapPriority).reversed());
        } else if ("dueDateAsc".equals(sortBy)) {
            tasks.sort(Comparator.comparing(Task::getDueDateTime));
        } else if ("dueDateDesc".equals(sortBy)) {
            tasks.sort(Comparator.comparing(Task::getDueDateTime).reversed());
        } else if ("statusAsc".equals(sortBy)) {
            tasks.sort(Comparator.comparingInt(this::mapStatus));
        } else if ("statusDesc".equals(sortBy)) {
            tasks.sort(Comparator.comparingInt(this::mapStatus).reversed());
        }
        return tasks;
    }

    private int mapPriority(Task task) {
        switch (task.getPriority()) {
            case LOW:
                return 1;
            case MEDIUM:
                return 2;
            case HIGH:
                return 3;
            default:
                return 0;
        }
    }

    private int mapStatus(Task task) {
        switch (task.getStatus()) {
            case NOT_STARTED:
                return 1;
            case IN_PROGRESS:
                return 2;
            case COMPLETED:
                return 3;
            default:
                return 0;
        }
    }
    public Page<TaskDTO> searchTasks(String query, int page, int size, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Pageable pageable = PageRequest.of(page, size);

        // Initialize enums as null
        Priority priority = null;
        Status status = null;

        // Attempt to convert query to Priority enum
        try {
            if (query != null && !query.isEmpty()) {
                priority = Priority.valueOf(query.toUpperCase());
            }
        } catch (IllegalArgumentException e) {
            // Handle conversion failure
        }

        // Attempt to convert query to Status enum
        try {
            if (query != null && !query.isEmpty()) {
                status = Status.valueOf(query.toUpperCase());
            }
        } catch (IllegalArgumentException e) {
            // Handle conversion failure
        }

        // If conversion fails, priority and status will remain null
        Page<Task> taskPage = taskRepository.searchTasksByUser(query, status, priority, userId, pageable);
        return taskPage.map(taskMapper::toDTO);
    }

    public Page<TaskDTO> filterAndSortTasks(Long id, String clientName, String taskTitle,
                                            String status, String priority, LocalDateTime dueDateTime,
                                            int page, int size, Sort sort, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Task> taskPage = taskRepository.filterAndSortTasksByUser(
                id, clientName, taskTitle, status, priority, dueDateTime, pageable, userId);
        return taskPage.map(taskMapper::toDTO);
    }
}