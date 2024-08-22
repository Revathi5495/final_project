package com.sails.client_connect.service;

import com.sails.client_connect.dto.AppointmentDTO;
import com.sails.client_connect.entity.Appointment;
import com.sails.client_connect.mapper.AppointmentMapper;
import com.sails.client_connect.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public List<AppointmentDTO> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(AppointmentMapper.INSTANCE::toAppointmentDTO)
                .collect(Collectors.toList());
    }

    public AppointmentDTO saveAppointment(AppointmentDTO appointmentDTO) {
        Appointment appointment = AppointmentMapper.INSTANCE.toAppointment(appointmentDTO);
        return AppointmentMapper.INSTANCE.toAppointmentDTO(appointmentRepository.save(appointment));
    }

}
