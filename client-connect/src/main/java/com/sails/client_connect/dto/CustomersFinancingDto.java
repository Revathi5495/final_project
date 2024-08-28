package com.sails.client_connect.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CustomersFinancingDto {
    @NotBlank(message = "First Name is required")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    private String lastName;
}
