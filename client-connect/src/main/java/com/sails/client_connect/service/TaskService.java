package com.sails.client_connect.service;

import com.sails.client_connect.dto.TaskDTO;
import com.sails.client_connect.entity.Customer;
import com.sails.client_connect.entity.Priority;
import com.sails.client_connect.entity.Task;
import com.sails.client_connect.entity.User;
import com.sails.client_connect.entity.RecurrencePattern;
import com.sails.client_connect.exception.UserNotFoundException;
import com.sails.client_connect.mapper.TaskMapper;
import com.sails.client_connect.repository.CustomerRepository;
import com.sails.client_connect.repository.TaskRepository;
import com.sails.client_connect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    private final TaskMapper taskMapper;

//    public List<TaskDTO> getAllTasks(String sortBy, String filterBy, String filterValue) {
//
//        List<Task> tasks = taskRepository.findByDeletedFalse();
//        // Implement sorting and filtering logic here
//        // Example: Sort by "dueDateTime", "priority", "status"
//        return taskMapper.toDTOList(tasks); // Example logic
//    }


    public TaskDTO getTaskById(Long id, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return taskRepository.findByIdAndUserAndDeletedFalse(id, user)
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
//        Task task = taskMapper.toEntity(taskDTO);
//        task.setCreatedDate(LocalDateTime.now());
//        Task savedTask = taskRepository.save(task);
//        return taskMapper.toDTO(savedTask);

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

//    public Optional<TaskDTO> updateTask(Long id, TaskDTO taskDTO) {
//        return taskRepository.findById(id)
//                .filter(task -> !task.isDeleted())
//                .map(existingTask -> {
//                    taskMapper.updateTaskFromDTO(taskDTO, existingTask);
//                    existingTask.setLastUpdatedDate(LocalDateTime.now());
//                    Task updatedTask = taskRepository.save(existingTask);
//                    return taskMapper.toDTO(updatedTask);
//                });
//    }
public TaskDTO patchUpdateTask(Long id, TaskDTO taskDTO) {
    User user = userRepository.findById(taskDTO.getUserId())
            .orElseThrow(() -> new UserNotFoundException("User not found"));
    Task task = taskRepository.findByIdAndUserAndDeletedFalse(id, user)
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
//    if(taskDTO.getRecurrencePattern()!=null){
//        task.setRecurrencePattern(taskDTO.getRecurrencePattern());
//    }
    try {
        task.setRecurrencePattern(RecurrencePattern.valueOf(taskDTO.getRecurrencePattern().name()));
    } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("Invalid recurrence pattern: " + taskDTO.getRecurrencePattern());
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

//    public void deleteTask(Long id) {
//        taskRepository.findById(id).map(task -> {
//            task.setDeleted(true); // Soft delete
//            taskRepository.save(task);
//            return true;
//        }).orElseThrow(() -> new UserNotFoundException("Task not found with id: " + id)); // Throw exception if task not found
//    }
    public void deleteTask(Long id, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        taskRepository.findByIdAndUserAndDeletedFalse(id, user)
                .map(task -> {
                    task.setDeleted(true); // Soft delete
                    taskRepository.save(task);
                    return true;
                })
                .orElseThrow(() -> new UserNotFoundException("Task not found with id: " + id)); // Throw exception if task not found
    }

    //    //indira
//    public List<TaskDTO> getAllTasks(String sortBy) {
//        List<Task> tasks;
//
//        if ("priorityAsc".equals(sortBy)) {
//            tasks = taskRepository.findByDeletedFalse(Sort.by(Sort.Direction.DESC, "priority"));
//
//        } else if ("priorityDesc".equals(sortBy)) {
//            tasks = taskRepository.findByDeletedFalse(Sort.by(Sort.Direction.ASC, "priority"));
//        } else {
//
//            tasks = taskRepository.findByDeletedFalse(Sort.unsorted()); // Default case, no sorting
//        }
//
//        return taskMapper.toDTOList(tasks);
//    }
//   }
//public List<TaskDTO> getTasksSortedByPriority(String sortBy) {
//    List<Task> tasks = taskRepository.findByDeletedFalse();
//
//    Comparator<Task> comparator = Comparator.comparingInt(task -> {
//        switch (task.getPriority()) {
//            case LOW:
//                return 1;
//            case MEDIUM:
//                return 2;
//            case HIGH:
//                return 3;
//            default:
//                return 0; // This default case handles any unexpected values
//        }
//    });
//
//    if ("priorityAsc".equals(sortBy)) {
//        tasks = tasks.stream().sorted(comparator).collect(Collectors.toList());
//    } else if ("priorityDesc".equals(sortBy)) {
//        tasks = tasks.stream().sorted(comparator.reversed()).collect(Collectors.toList());
//    }
//
//    return taskMapper.toDTOList(tasks);
//}
//    public List<TaskDTO> getTasksSortedByDueDate(String sortBy) {
//        List<Task> tasks = taskRepository.findByDeletedFalse();
//
//        Comparator<Task> comparator = Comparator.comparing(Task::getDueDateTime);
//
//        if ("dueDateAsc".equals(sortBy)) {
//            tasks = tasks.stream().sorted(comparator).collect(Collectors.toList());
//        } else if ("dueDateDesc".equals(sortBy)) {
//            tasks = tasks.stream().sorted(comparator.reversed()).collect(Collectors.toList());
//        }
//
//        return taskMapper.toDTOList(tasks);
//    }
//    public List<TaskDTO> getTasksSortedByStatus(String sortBy) {
//        List<Task> tasks = taskRepository.findByDeletedFalse();
//
//        // Define the order for ascending sorting
//        Comparator<Task> comparator = Comparator.comparing(task -> {
//            switch (task.getStatus()) {
//                case NOT_STARTED:
//                    return 1;
//                case IN_PROGRESS:
//                    return 2;
//                case COMPLETED:
//                    return 3;
//                default:
//                    return 0; // Default case if the status is not recognized
//            }
//        });
//
//        if ("statusAsc".equals(sortBy)) {
//            tasks = tasks.stream().sorted(comparator).collect(Collectors.toList());
//        } else if ("statusDesc".equals(sortBy)) {
//            tasks = tasks.stream().sorted(comparator.reversed()).collect(Collectors.toList());
//        }
//
//        return taskMapper.toDTOList(tasks);
//    }
//    public List<TaskDTO> getTasksByPriority(Priority priority) {
//        List<Task> tasks = taskRepository.findByDeletedFalseAndPriority(priority);
//        return taskMapper.toDTOList(tasks);
//    }
    public List<TaskDTO> getAllTasks(Long userId, String sortBy, String filterBy, String filterValue) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<Task> tasks = taskRepository.findByUserAndDeletedFalse(user);

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
}