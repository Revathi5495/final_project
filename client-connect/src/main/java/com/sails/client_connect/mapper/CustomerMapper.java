package com.sails.client_connect.mapper;

import com.sails.client_connect.dto.CustomerDTO;
import com.sails.client_connect.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(source = "userId", target = "user.user_id")
    Customer toEntity(CustomerDTO customerDTO);

    @Mapping(source = "user.user_id", target = "userId")
    CustomerDTO toDto(Customer customer);
}
