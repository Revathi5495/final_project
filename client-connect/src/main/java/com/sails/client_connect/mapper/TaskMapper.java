package com.sails.client_connect.mapper;

import com.sails.client_connect.dto.TaskDTO;
import com.sails.client_connect.dto.TaskUpdateDTO;
import com.sails.client_connect.entity.Task;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CustomerMapper.class})
public interface TaskMapper {

    @Mapping(source = "user.user_id", target = "userId")
    @Mapping(source = "customer.id", target = "customerId")
    TaskDTO toDTO(Task task);

    @Mapping(source = "user.user_id", target = "userId")
    @Mapping(source = "customer.id", target = "customerId")
    List<TaskDTO> toDTOList(List<Task> taskList);

    @Mapping(source = "userId", target = "user.user_id")
    @Mapping(source = "customerId", target = "customer.id")
    Task toEntity(TaskDTO taskDTO);


    List<TaskUpdateDTO> toUpdateDTOList(List<Task> taskList);

}

