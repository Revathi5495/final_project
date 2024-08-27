// AppointmentDTO.java
package com.sails.client_connect.dto;

import com.sails.client_connect.entity.RecurrencePattern;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AppointmentDTO {
    private String title;
    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String location;
    private List<String> attendees;
    private Long customerId;
    //private CustomerDTO customer;  // Include the full CustomerDTO instead of just the ID
    private RecurrencePattern recurrencePattern;
    private Long taskId;
    private Long userId;
}
