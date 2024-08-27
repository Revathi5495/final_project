package com.sails.client_connect.mapper;

import com.sails.client_connect.dto.UserDto;
import com.sails.client_connect.entity.User;

public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(UserDto userDto);
}
