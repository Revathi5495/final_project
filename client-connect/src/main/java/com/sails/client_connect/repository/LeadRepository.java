package com.sails.client_connect.repository;

import com.sails.client_connect.entity.Lead;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeadRepository extends JpaRepository<Lead,Long> {
}
