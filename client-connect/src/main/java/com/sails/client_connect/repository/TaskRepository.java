package com.sails.client_connect.repository;

import com.sails.client_connect.entity.Priority;
import com.sails.client_connect.entity.Task;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
       List<Task> findByDeletedFalse();
//    Optional<Task> findByIdAndDeletedFalse(Long id);
//    public interface TaskRepository extends JpaRepository<Task, Long> {
        List<Task> findByDeletedFalse(Sort sort);
        Optional<Task> findByIdAndDeletedFalse(Long id);
    List<Task> findByDeletedFalseAndPriority(Priority priority);}

