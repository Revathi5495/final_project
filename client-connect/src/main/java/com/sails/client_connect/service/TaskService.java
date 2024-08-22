package com.sails.client_connect.service;

import com.sails.client_connect.dto.TaskDTO;
import com.sails.client_connect.entity.Customer;
import com.sails.client_connect.entity.Priority;
import com.sails.client_connect.entity.Task;
import com.sails.client_connect.entity.User;
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

    public List<TaskDTO> getAllTasks(String sortBy, String filterBy, String filterValue) {

        List<Task> tasks = taskRepository.findByDeletedFalse();
        // Implement sorting and filtering logic here
        // Example: Sort by "dueDateTime", "priority", "status"
        return taskMapper.toDTOList(tasks); // Example logic
    }


    public Optional<TaskDTO> getTaskById(Long id) {
        return taskRepository.findByIdAndDeletedFalse(id)
                .map(taskMapper::toDTO);
    }

    public TaskDTO createTask(TaskDTO taskDTO) {
//        Task task = taskMapper.toEntity(taskDTO);
//        task.setCreatedDate(LocalDateTime.now());
//        Task savedTask = taskRepository.save(task);
//        return taskMapper.toDTO(savedTask);

        User assignedTo = userRepository.findById(taskDTO.getAssignedToId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Customer customer = customerRepository.findById(taskDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        // Convert DTO to Entity and set additional fields
        Task task = taskMapper.toEntity(taskDTO);
        task.setAssignedTo(assignedTo);
        task.setCustomer(customer);
        task.setCreatedDate(LocalDateTime.now());
        task.setLastUpdatedDate(LocalDateTime.now());

        // Save the entity
        Task savedTask = taskRepository.save(task);

        // Convert saved entity to DTO and return
        return taskMapper.toDTO(savedTask);
    }

    public Optional<TaskDTO> updateTask(Long id, TaskDTO taskDTO) {
        return taskRepository.findById(id)
                .filter(task -> !task.isDeleted())
                .map(existingTask -> {
                    taskMapper.updateTaskFromDTO(taskDTO, existingTask);
                    existingTask.setLastUpdatedDate(LocalDateTime.now());
                    Task updatedTask = taskRepository.save(existingTask);
                    return taskMapper.toDTO(updatedTask);
                });
    }

    public boolean deleteTask(Long id) {
        return taskRepository.findById(id).map(task -> {
            task.setDeleted(true); // Soft delete
            taskRepository.save(task);
            return true;
        }).orElse(false);
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
public List<TaskDTO> getTasksSortedByPriority(String sortBy) {
    List<Task> tasks = taskRepository.findByDeletedFalse();

    Comparator<Task> comparator = Comparator.comparingInt(task -> {
        switch (task.getPriority()) {
            case LOW:
                return 1;
            case MEDIUM:
                return 2;
            case HIGH:
                return 3;
            default:
                return 0; // This default case handles any unexpected values
        }
    });

    if ("priorityAsc".equals(sortBy)) {
        tasks = tasks.stream().sorted(comparator).collect(Collectors.toList());
    } else if ("priorityDesc".equals(sortBy)) {
        tasks = tasks.stream().sorted(comparator.reversed()).collect(Collectors.toList());
    }

    return taskMapper.toDTOList(tasks);
}
    public List<TaskDTO> getTasksSortedByDueDate(String sortBy) {
        List<Task> tasks = taskRepository.findByDeletedFalse();

        Comparator<Task> comparator = Comparator.comparing(Task::getDueDateTime);

        if ("dueDateAsc".equals(sortBy)) {
            tasks = tasks.stream().sorted(comparator).collect(Collectors.toList());
        } else if ("dueDateDesc".equals(sortBy)) {
            tasks = tasks.stream().sorted(comparator.reversed()).collect(Collectors.toList());
        }

        return taskMapper.toDTOList(tasks);
    }
    public List<TaskDTO> getTasksSortedByStatus(String sortBy) {
        List<Task> tasks = taskRepository.findByDeletedFalse();

        // Define the order for ascending sorting
        Comparator<Task> comparator = Comparator.comparing(task -> {
            switch (task.getStatus()) {
                case NOT_STARTED:
                    return 1;
                case IN_PROGRESS:
                    return 2;
                case COMPLETED:
                    return 3;
                default:
                    return 0; // Default case if the status is not recognized
            }
        });

        if ("statusAsc".equals(sortBy)) {
            tasks = tasks.stream().sorted(comparator).collect(Collectors.toList());
        } else if ("statusDesc".equals(sortBy)) {
            tasks = tasks.stream().sorted(comparator.reversed()).collect(Collectors.toList());
        }

        return taskMapper.toDTOList(tasks);
    }
    public List<TaskDTO> getTasksByPriority(Priority priority) {
        List<Task> tasks = taskRepository.findByDeletedFalseAndPriority(priority);
        return taskMapper.toDTOList(tasks);
}}