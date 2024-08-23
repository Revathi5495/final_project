package com.sails.client_connect.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class CustomerDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;

   // private List<AppointmentDTO> appointments;
}
