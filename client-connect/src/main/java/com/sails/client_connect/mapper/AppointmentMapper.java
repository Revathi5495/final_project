package com.sails.client_connect.mapper;

import com.sails.client_connect.dto.AppointmentDTO;
import com.sails.client_connect.entity.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AppointmentMapper {
    AppointmentMapper INSTANCE = Mappers.getMapper(AppointmentMapper.class);

    AppointmentDTO toAppointmentDTO(Appointment appointment);

    Appointment toAppointment(AppointmentDTO appointmentDTO);
}
