package com.sails.client_connect.service;

import com.sails.client_connect.dto.AppointmentDTO;
import com.sails.client_connect.entity.Appointment;
import com.sails.client_connect.entity.Customer;
import com.sails.client_connect.entity.RecurrencePattern;
import com.sails.client_connect.entity.User;
import com.sails.client_connect.repository.UserRepository;
import com.sails.client_connect.service.ResourceNotFoundException;
import com.sails.client_connect.mapper.AppointmentMapper;
import com.sails.client_connect.repository.AppointmentRepository;
import com.sails.client_connect.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

//    @Autowired
//    private AppointmentRepository appointmentRepository;
//    private final CustomerRepository customerRepository;
//
//    private final AppointmentMapper mapper = AppointmentMapper.INSTANCE;
//    @Autowired
//    public AppointmentService(AppointmentRepository appointmentRepository, CustomerRepository customerRepository) {
//        this.appointmentRepository = appointmentRepository;
//        this.customerRepository = customerRepository;
//    }
//
//    public AppointmentDTO createAppointment(AppointmentDTO dto) {
//        Appointment appointment = mapper.toEntity(dto);
//
//        // Correct context: Using findById in a non-static method
//        Customer customer = customerRepository.findById((long) dto.getCustomerId())
//                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
//
//        appointment.setCustomer(customer);
//
//        return mapper.toDto(appointmentRepository.save(appointment));
//    }
//
//    public AppointmentDTO updateAppointment(Long id, AppointmentDTO dto) {
//        Appointment appointment = appointmentRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
//
//        appointment.setTitle(dto.getTitle());
//        appointment.setDescription(dto.getDescription());
//        appointment.setStartDateTime(dto.getStartDateTime());
//        appointment.setEndDateTime(dto.getEndDateTime());
//        appointment.setLocation(dto.getLocation());
//        appointment.setAttendees(dto.getAttendees());
//
//        // Convert string to enum
//        try {
//            appointment.setRecurrencePattern(RecurrencePattern.valueOf(dto.getRecurrencePattern().name()));
//        } catch (IllegalArgumentException e) {
//            throw new IllegalArgumentException("Invalid recurrence pattern: " + dto.getRecurrencePattern());
//        }
//
//        return mapper.toDto(appointmentRepository.save(appointment));
//    }
//
//
//
//    public void deleteAppointment(Long id) {
//        if (!appointmentRepository.existsById(id)) {
//            throw new ResourceNotFoundException("Appointment not found");
//        }
//        appointmentRepository.deleteById(id);
//    }
//
//    public AppointmentDTO getAppointment(Long id) {
//        Appointment appointment = appointmentRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
//        return mapper.toDto(appointment);
//    }
//
//    public List<AppointmentDTO> getAllAppointments() {
//        return appointmentRepository.findAll().stream()
//                .map(mapper::toDto)
//                .collect(Collectors.toList());
//    }
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private UserRepository userRepository;

    private final AppointmentMapper mapper = AppointmentMapper.INSTANCE;

    public AppointmentDTO createAppointment(AppointmentDTO dto, Long userId) {
        Appointment appointment = mapper.toEntity(dto);

        Customer customer = customerRepository.findById((long) dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        appointment.setCustomer(customer);
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        appointment.setUser(user);
        return mapper.toDto(appointmentRepository.save(appointment));
    }

    public AppointmentDTO updateAppointment(Long id, AppointmentDTO dto, Long userId) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        // Ensure the appointment belongs to the user
        if (!appointment.getUser().getUser_id().equals(userId)) {
            throw new ResourceNotFoundException("Appointment not found for the current user");
        }

        appointment.setTitle(dto.getTitle());
        appointment.setDescription(dto.getDescription());
        appointment.setStartDateTime(dto.getStartDateTime());
        appointment.setEndDateTime(dto.getEndDateTime());
        appointment.setLocation(dto.getLocation());
        appointment.setAttendees(dto.getAttendees());

        try {
            appointment.setRecurrencePattern(RecurrencePattern.valueOf(dto.getRecurrencePattern().name()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid recurrence pattern: " + dto.getRecurrencePattern());
        }

        return mapper.toDto(appointmentRepository.save(appointment));
    }

    public void deleteAppointment(Long id, Long userId) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        if (!appointment.getUser().getUser_id().equals(userId)) {
            throw new ResourceNotFoundException("Appointment not found for the current user");
        }

        appointmentRepository.deleteById(id);
    }

    public AppointmentDTO getAppointment(Long id, Long userId) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        if (!appointment.getUser().getUser_id().equals(userId)) {
            throw new ResourceNotFoundException("Appointment not found for the current user");
        }

        return mapper.toDto(appointment);
    }

    public List<AppointmentDTO> getAllAppointments(Long userId) {
        return appointmentRepository.findAll().stream()
                .filter(appointment -> appointment.getUser().getUser_id().equals(userId))
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

}
