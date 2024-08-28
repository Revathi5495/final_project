package com.sails.client_connect.mapper;

import com.sails.client_connect.dto.RoleDto;
import com.sails.client_connect.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    Role toEntity(RoleDto roleDTO);

    RoleDto toDto(Role role);
}
