// AppointmentDTO.java
package com.sails.client_connect.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sails.client_connect.entity.RecurrencePattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentUpdateDTO {
    private String title;
    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String location;
    private List<String> attendees;

    private RecurrencePattern recurrencePattern;
}
