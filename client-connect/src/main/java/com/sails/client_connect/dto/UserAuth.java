package com.sails.client_connect.dto;



import com.sails.client_connect.entity.RoleName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class UserAuth {
    private String username;
    private String email;
    private String password;
    private Set<RoleName> roleNames;

}
