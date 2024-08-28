package com.sails.client_connect.controller;

import com.sails.client_connect.dto.AppointmentDTO;
import com.sails.client_connect.response.ApiResponse;
import com.sails.client_connect.service.AppointmentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;


//    @PostMapping
//    public ResponseEntity<AppointmentDTO> createAppointment(@Valid @RequestBody AppointmentDTO appointmentDTO, HttpSession session) {
//        Long userId = (Long) session.getAttribute("userId");
//        appointmentDTO.setUserId(userId);
//        return new ResponseEntity<>(appointmentService.createAppointment(appointmentDTO), HttpStatus.CREATED);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<AppointmentDTO> updateAppointment(@PathVariable Long id, @RequestBody AppointmentDTO appointmentDTO,HttpSession session) {
//        Long userId = (Long) session.getAttribute("userId");
//        if(userId!=null){
//            appointmentDTO.setUserId(userId);
//        }
//        return new ResponseEntity<>(appointmentService.updateAppointment(id, appointmentDTO), HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
//        appointmentService.deleteAppointment(id);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<AppointmentDTO> getAppointment(@PathVariable Long id) {
//        return new ResponseEntity<>(appointmentService.getAppointment(id), HttpStatus.OK);
//    }
//
//    @GetMapping
//    public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
//        return new ResponseEntity<>(appointmentService.getAllAppointments(), HttpStatus.OK);
//    }
@PostMapping
public ResponseEntity<ApiResponse<AppointmentDTO>> createAppointment(
        @Valid @RequestBody AppointmentDTO appointmentDTO, HttpSession session) {
    Long userId = (Long) session.getAttribute("userId");
    appointmentDTO.setUserId(userId);
    AppointmentDTO createdAppointment = appointmentService.createAppointment(appointmentDTO, userId);
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(new ApiResponse<>("Appointment created successfully", HttpStatus.CREATED, createdAppointment, null));
}

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AppointmentDTO>> updateAppointment(
            @PathVariable Long id, @RequestBody AppointmentDTO appointmentDTO, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        appointmentDTO.setUserId(userId);
        AppointmentDTO updatedAppointment = appointmentService.updateAppointment(id, appointmentDTO, userId);
        return ResponseEntity.ok(new ApiResponse<>("Appointment updated successfully", HttpStatus.OK, updatedAppointment, null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAppointment(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        appointmentService.deleteAppointment(id, userId);
        return ResponseEntity.ok(new ApiResponse<>("Appointment deleted successfully", HttpStatus.NO_CONTENT, null, null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AppointmentDTO>> getAppointment(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        AppointmentDTO appointment = appointmentService.getAppointment(id, userId);
        return ResponseEntity.ok(new ApiResponse<>("Appointment retrieved successfully", HttpStatus.OK, appointment, null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AppointmentDTO>>> getAllAppointments(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        List<AppointmentDTO> appointments = appointmentService.getAllAppointments(userId);
        return ResponseEntity.ok(new ApiResponse<>("Appointments retrieved successfully", HttpStatus.OK, appointments, null));
    }
}
