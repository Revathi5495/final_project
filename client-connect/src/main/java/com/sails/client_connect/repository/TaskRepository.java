package com.sails.client_connect.repository;

import com.sails.client_connect.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task,Long> {
}
