package com.sails.client_connect.dto;



import com.sails.client_connect.entity.RoleName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuth {
    private String username;
    private String email;
    private String password;
    private Set<RoleName> roleNames;

}
