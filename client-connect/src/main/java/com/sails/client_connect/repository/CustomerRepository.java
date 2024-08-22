package com.sails.client_connect.repository;


import com.sails.client_connect.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c FROM Customer c WHERE " +
            "c.firstName LIKE %:keyword% OR " +
            "c.lastName LIKE %:keyword% OR " +
            "c.email LIKE %:keyword% OR " +
            "c.phoneNumber LIKE %:keyword% OR " +
            "c.address LIKE %:keyword%")
    List<Customer> searchCustomers(@Param("keyword") String keyword);
}
