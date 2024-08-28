package com.sails.client_connect.repository;

import com.sails.client_connect.entity.Priority;
import com.sails.client_connect.entity.Task;
import com.sails.client_connect.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
//       List<Task> findByDeletedFalse();
////    Optional<Task> findByIdAndDeletedFalse(Long id);
////    public interface TaskRepository extends JpaRepository<Task, Long> {
//        List<Task> findByDeletedFalse(Sort sort);
//        Optional<Task> findByIdAndDeletedFalse(Long id);
//    List<Task> findByDeletedFalseAndPriority(Priority priority);
//    List<Task> findByUserAndDeletedFalse(User user);
//    Optional<Task> findByIdAndUserAndDeletedFalse(Long id, User user);
    Optional<Task> findByIdAndUser(Long id, User user);
    List<Task> findByUser(User user);
}



