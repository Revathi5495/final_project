package com.sails.client_connect.mapper;

import com.sails.client_connect.dto.CustomerDTO;
import com.sails.client_connect.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDTO toDto(Customer customer);

    Customer toEntity(CustomerDTO customerDTO);
}
