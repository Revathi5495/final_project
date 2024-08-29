package com.sails.client_connect.repository;

import com.sails.client_connect.dto.AppointmentDTO;
import com.sails.client_connect.entity.Appointment;
import com.sails.client_connect.entity.Lead;
import com.sails.client_connect.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a WHERE " +
            "(LOWER(a.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(a.description) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "a.location LIKE CONCAT('%', :query, '%')) AND " +
            "a.user.user_id = :userId")
    Page<Appointment> searchAppointmentsByUser(@Param("query") String query, @Param("userId") Long userId, Pageable pageable);

    @Query("SELECT a FROM Appointment a WHERE " +
            "(:id IS NULL OR a.id = :id) AND " +
            "(:title IS NULL OR LOWER(a.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:description IS NULL OR LOWER(a.description) LIKE LOWER(CONCAT('%', :description, '%'))) AND " +
            "(:location IS NULL OR a.location LIKE CONCAT('%', :location, '%')) AND " +
            "(:startDateTime IS NULL OR a.startDateTime = :startDateTime) AND " +
            "(:endDateTime IS NULL OR a.endDateTime = :endDateTime) AND " +
            "a.user.user_id = :userId")
    Page<Appointment> filterAndSortAppointmentsByUser(@Param("id") Long id,
                                                      @Param("title") String title,
                                                      @Param("description") String description,
                                                      @Param("location") String location,
                                                      @Param("startDateTime") LocalDateTime startDateTime,
                                                      @Param("endDateTime") LocalDateTime endDateTime,
                                                      Pageable pageable, @Param("userId") Long userId);

    List<Appointment> findAllByUser(User user);


}
