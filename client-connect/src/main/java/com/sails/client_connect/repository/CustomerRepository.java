package com.sails.client_connect.repository;


import com.sails.client_connect.entity.Customer;
import com.sails.client_connect.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByIdAndUser(Long id, User user);

    List<Customer> findAllByUser(User user);

    //Page<Customer> searchCustomersByUser(String query, User user, Pageable pageable);

//    Page<Customer> filterAndSortCustomersByUser(
//            Long id, String firstName, String lastName, String email,
//            String phoneNumber, String address, Pageable pageable, User user);

    //    @Query("SELECT c FROM Customer c WHERE " +
//            "c.firstName LIKE %:keyword% OR " +
//            "c.lastName LIKE %:keyword% OR " +
//            "c.email LIKE %:keyword% OR " +
//            "c.phoneNumber LIKE %:keyword% OR " +
//            "c.address LIKE %:keyword%")
//    List<Customer> searchCustomers(@Param("keyword") String keyword);
    @Query("SELECT c FROM Customer c WHERE " +
            "(LOWER(c.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(CONCAT(c.firstName, ' ', c.lastName)) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(c.email) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "c.phoneNumber LIKE CONCAT('%', :query, '%') OR " +
            "LOWER(c.address) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
            "c.user = :user")
    Page<Customer> searchCustomersByUser(@Param("query") String query, @Param("user") User user, Pageable pageable);

    // Filter and Sort with Pagination
//    @Query("SELECT c FROM Customer c WHERE " +
//            "(:id IS NULL OR c.id = :id) AND " +
//            "(:firstName IS NULL OR LOWER(c.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) AND " +
//            "(:lastName IS NULL OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) AND " +
//            "(:email IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
//            "(:phoneNumber IS NULL OR c.phoneNumber LIKE CONCAT('%', :phoneNumber, '%')) AND " +
//            "(:address IS NULL OR LOWER(c.address) LIKE LOWER(CONCAT('%', :address, '%')))")
//    Page<Customer> filterAndSortCustomers(@Param("id") Long id,
//                                          @Param("firstName") String firstName,
//                                          @Param("lastName") String lastName,
//                                          @Param("email") String email,
//                                          @Param("phoneNumber") String phoneNumber,
//                                          @Param("address") String address,
//                                          Pageable pageable);
    @Query("SELECT c FROM Customer c WHERE " +
            "(:id IS NULL OR c.id = :id) AND " +
            "(:firstName IS NULL OR LOWER(c.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) AND " +
            "(:lastName IS NULL OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) AND " +
            "(:email IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
            "(:phoneNumber IS NULL OR c.phoneNumber LIKE CONCAT('%', :phoneNumber, '%')) AND " +
            "(:address IS NULL OR LOWER(c.address) LIKE LOWER(CONCAT('%', :address, '%'))) AND " +
            "c.user = :user")
    Page<Customer> filterAndSortCustomersByUser(@Param("id") Long id,
                                                @Param("firstName") String firstName,
                                                @Param("lastName") String lastName,
                                                @Param("email") String email,
                                                @Param("phoneNumber") String phoneNumber,
                                                @Param("address") String address,
                                                Pageable pageable, @Param("user") User user);

}
