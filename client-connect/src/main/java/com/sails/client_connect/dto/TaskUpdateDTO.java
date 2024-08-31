package com.sails.client_connect.dto;

import com.sails.client_connect.entity.Priority;
import com.sails.client_connect.entity.RecurrencePattern;
import com.sails.client_connect.entity.Status;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskUpdateDTO {

    private String clientName;

    @NotBlank(message = "Task Title is required")
    private String taskTitle;

    @Size(max = 500, message = "Description can be up to 500 characters long")
    private String description;

    @NotNull(message = "Due Date and Time is required")
    @Future(message = "Due Date and Time should be in future")
    private LocalDateTime dueDateTime;

    @NotEmpty(message = "Priority is required")
    private Priority priority;

    @NotEmpty(message = "Status is required")
    private Status status;

    @NotEmpty(message = "Recurrence Pattern is required")
    private RecurrencePattern recurrencePattern;

    private LocalDateTime createdDate;

    private LocalDateTime lastUpdatedDate;
}
