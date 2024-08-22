package com.sails.client_connect.dto;

import lombok.Data;

@Data
public class LeadDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String phoneNumber;
   // private String address;
}
