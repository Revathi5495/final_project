package com.sails.client_connect.repository;

import com.sails.client_connect.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
