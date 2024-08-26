package com.sails.client_connect.mapper;

import com.sails.client_connect.dto.TaskDTO;
import com.sails.client_connect.entity.Task;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CustomerMapper.class})
public interface TaskMapper {

    @Mapping(source = "assignedTo.user_id", target = "assignedToId")
    @Mapping(source = "customer.id", target = "customerId")
    TaskDTO toDTO(Task task);

    @Mapping(source = "assignedTo.user_id", target = "assignedToId")
    @Mapping(source = "customer.id", target = "customerId")
    List<TaskDTO> toDTOList(List<Task> taskList);

    @Mapping(source = "assignedToId", target = "assignedTo.user_id")
    @Mapping(source = "customerId", target = "customer.id")
    Task toEntity(TaskDTO taskDTO);

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "createdDate", ignore = true)
//    @Mapping(target = "lastUpdatedDate", ignore = true)
//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    void updateTaskFromDTO(TaskDTO taskDTO, @MappingTarget Task task);
}

