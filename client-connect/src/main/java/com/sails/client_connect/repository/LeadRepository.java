package com.sails.client_connect.repository;

import com.revathi.taskdairymanagement.entity.Lead;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LeadRepository extends JpaRepository<Lead, Long> {
}
