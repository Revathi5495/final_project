package com.sails.client_connect.dto;


import com.sails.client_connect.entity.RoleName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class UserDto {

    private int user_id;
    private String username;
    private String email;
    private Set<RoleName> roleNames;
}
