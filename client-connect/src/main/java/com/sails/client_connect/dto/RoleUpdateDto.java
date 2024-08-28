package com.sails.client_connect.dto;


import com.sails.client_connect.entity.RoleName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleUpdateDto {
    private Long role_id;
    private RoleName name;
}
