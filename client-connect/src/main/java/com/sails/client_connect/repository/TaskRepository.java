package com.sails.client_connect.repository;

import com.revathi.taskdairymanagement.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task,Long> {
}
