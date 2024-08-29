package com.sails.client_connect.dto;

import com.sails.client_connect.entity.RoleName;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RoleDTO {

    private Long role_id;

    private RoleName name;
}

