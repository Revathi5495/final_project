//package com.sails.client_connect.mapper;
//
//import com.sails.client_connect.dto.AppointmentDTO;
//import com.sails.client_connect.entity.Appointment;
//import org.mapstruct.Mapper;
//import org.mapstruct.factory.Mappers;
//
//@Mapper(componentModel = "spring")
//public interface AppointmentMapper {
//
//    AppointmentDTO toAppointmentDTO(Appointment appointment);
//
//    Appointment toAppointment(AppointmentDTO appointmentDTO);
//}
package com.sails.client_connect.mapper;

import com.sails.client_connect.dto.AppointmentDTO;
import com.sails.client_connect.entity.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AppointmentMapper {

    AppointmentMapper INSTANCE = Mappers.getMapper(AppointmentMapper.class);

    @Mapping(source = "task.id",target = "taskId")
    @Mapping(source = "user.user_id",target = "userId")
    @Mapping(source = "customer.id",target = "customerId")
    @Mapping(target = "recurrencePattern", source = "recurrencePattern")
    AppointmentDTO toDto(Appointment appointment);

    @Mapping(source = "taskId",target = "task.id")
    @Mapping(source = "userId",target = "user.user_id")
    @Mapping(source = "customerId",target = "customer.id")
    @Mapping(target = "recurrencePattern", source = "recurrencePattern")
    Appointment toEntity(AppointmentDTO appointmentDTO);
}
