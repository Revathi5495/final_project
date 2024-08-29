package com.sails.client_connect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleUpdateDTO {
    private Long user_id;
    private String username;
    private String email;
    private Set<RoleUpdateDTO> roleNames;

}
