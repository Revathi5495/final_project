package com.sails.client_connect.controller;

import com.sails.client_connect.dto.CustomerDTO;
import com.sails.client_connect.response.ApiResponse;
import com.sails.client_connect.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    //    @PostMapping("/create-customer")
//    public ResponseEntity<ApiResponse<CustomerDTO>> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
//        CustomerDTO createdCustomer = customerService.createCustomer(customerDTO);
//        ApiResponse<CustomerDTO> response = new ApiResponse<>(
//                "Customer created successfully",
//                HttpStatus.CREATED,
//                createdCustomer
//        );
//        return new ResponseEntity<>(response, HttpStatus.CREATED);
//    }
    @PostMapping("/create-customer")
    public ResponseEntity<ApiResponse<CustomerDTO>> createCustomer(
            @Valid @RequestBody CustomerDTO customerDTO,
            HttpSession session) {

        // Retrieve the user_id from the session
        Long userId = (Long) session.getAttribute("userId");

        // Set the user_id in the customerDTO
        customerDTO.setUserId(userId);

        // Create the customer
        CustomerDTO createdCustomer = customerService.createCustomer(customerDTO);
        ApiResponse<CustomerDTO> response = new ApiResponse<>(
                "Customer created successfully",
                HttpStatus.CREATED,
                createdCustomer
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id,HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        CustomerDTO customerDTO = customerService.getCustomerByIdAndUserId(id, userId);
        return new ResponseEntity<>(customerDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        List<CustomerDTO> customers = customerService.getAllCustomersByUserId(userId);
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerDTO>> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerDTO customerDTO,
            HttpSession session) {

        // Retrieve the user_id from the session
        Long userId = (Long) session.getAttribute("userId");
        customerDTO.setUserId(userId);


        // Update the customer
        CustomerDTO updatedCustomer = customerService.updateCustomer(id, customerDTO);
        ApiResponse<CustomerDTO> response = new ApiResponse<>(
                "Customer updated successfully",
                HttpStatus.OK,
                updatedCustomer
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable Long id,HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        customerService.deleteCustomer(id, userId);
        ApiResponse<Void> response = new ApiResponse<>(
                "Customer deleted successfully",
                HttpStatus.OK,
                null
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //    @GetMapping("/search")
//    public ResponseEntity<List<CustomerDTO>> searchCustomers(@RequestParam(required = false) String query) {
//        List<CustomerDTO> customers = customerService.searchCustomers(query != null ? query : "");
//        return new ResponseEntity<>(customers, HttpStatus.OK);
//    }
    @GetMapping("/search")
    public ResponseEntity<Page<CustomerDTO>> searchCustomers(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        Page<CustomerDTO> customerPage = customerService.searchCustomers(query, page, size, userId);
        return ResponseEntity.ok(customerPage);

    }

    @GetMapping("/filter-sort")
    public ResponseEntity<Page<CustomerDTO>> filterAndSortCustomers(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String address,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "firstName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Page<CustomerDTO> customerPage = customerService.filterAndSortCustomers(
                id, firstName, lastName, email, phoneNumber, address, page, size, sort, userId);
        return ResponseEntity.ok(customerPage);
    }

}

