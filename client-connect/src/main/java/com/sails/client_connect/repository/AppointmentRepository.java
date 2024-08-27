package com.sails.client_connect.repository;

import com.sails.client_connect.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    //List<Appointment> findByUsername(String username);
    //List<Appointment> findByCustomerNameContainingIgnoreCase(String customerName);
}
