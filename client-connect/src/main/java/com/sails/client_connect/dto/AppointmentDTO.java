// AppointmentDTO.java
package com.sails.client_connect.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AppointmentDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String location;
    private List<String> attendees;
    private CustomerDTO customer;  // Include the full CustomerDTO instead of just the ID
    private String recurrencePattern;
}
