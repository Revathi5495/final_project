package com.sails.client_connect.service;

import com.sails.client_connect.dto.CustomerDTO;
import com.sails.client_connect.entity.Customer;
import com.sails.client_connect.mapper.CustomerMapper;
import com.sails.client_connect.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Customer customer = customerMapper.toEntity(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toDto(savedCustomer);
    }

    public CustomerDTO getCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(customerMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Customer not found with id " + id));
    }

    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::toDto)
                .collect(Collectors.toList());
    }

    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id " + id));

        // Update the customer's fields if they are present in the DTO
        if (customerDTO.getFirstName() != null) {
            customer.setFirstName(customerDTO.getFirstName());
        }
        if (customerDTO.getLastName() != null) {
            customer.setLastName(customerDTO.getLastName());
        }
        if (customerDTO.getEmail() != null) {
            customer.setEmail(customerDTO.getEmail());
        }
        if (customerDTO.getAddress() != null) {
            customer.setAddress(customerDTO.getAddress());
        }
        if (customerDTO.getPhoneNumber() != null) {
            customer.setPhoneNumber(customerDTO.getPhoneNumber());
        }

        Customer updatedCustomer = customerRepository.save(customer);
        return customerMapper.toDto(updatedCustomer);
    }


    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("Customer not found with id " + id);
        }
        customerRepository.deleteById(id);
    }

//    public List<CustomerDTO> searchCustomers(String query) {
//        List<Customer> customers = customerRepository.searchCustomers(query);
//        return customers.stream()
//                .map(customerMapper::toDto)
//                .collect(Collectors.toList());
//    }

    public Page<CustomerDTO> searchCustomers(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customerPage = customerRepository.searchCustomers(query,pageable );
        return customerPage.map(this::convertToDTO);
    }


    public Page<CustomerDTO> filterAndSortCustomers(Long id, String firstName, String lastName,
                                                    String email, String phoneNumber, String address,
                                                    int page, int size, Sort sort) {
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Customer> customerPage = customerRepository.filterAndSortCustomers(id, firstName, lastName, email, phoneNumber, address, pageable);
        return customerPage.map(this::convertToDTO);
    }
    private CustomerDTO convertToDTO(Customer customer) {
        return CustomerDTO.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .address(customer.getAddress())
                .build();
    }
}
