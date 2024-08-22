package com.sails.client_connect.dto;

import lombok.Data;
import java.util.List;

@Data
public class CustomerDTO {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private List<AppointmentDTO> appointments;
}
