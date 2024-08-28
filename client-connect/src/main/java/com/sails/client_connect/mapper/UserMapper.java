package com.sails.client_connect.mapper;

import com.sails.client_connect.dto.RoleUpdateDto;
import com.sails.client_connect.dto.UserDto;
import com.sails.client_connect.dto.UserRoleUpdateDto;
import com.sails.client_connect.entity.Role;
import com.sails.client_connect.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(UserDto userDto);

    @Mapping(source = "roles", target = "roleNames")
    UserRoleUpdateDto toUpdateDto(User user);

    default Set<RoleUpdateDto> mapRolesToRoleNames(Set<Role> roles) {
        if (roles == null) {
            return null;
        }
        return roles.stream()
                .map(this::mapRoleToRoleUpdateDto)
                .collect(Collectors.toSet());
    }

    default RoleUpdateDto mapRoleToRoleUpdateDto(Role role) {
        if (role == null) {
            return null;
        }
        return new RoleUpdateDto(role.getRole_id(), role.getName());
    }
}
