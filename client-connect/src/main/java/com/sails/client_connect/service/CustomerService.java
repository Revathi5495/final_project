package com.sails.client_connect.service;

import com.sails.client_connect.dto.CustomerDTO;
import com.sails.client_connect.entity.Customer;
import com.sails.client_connect.entity.User;
import com.sails.client_connect.exception.UserNotFoundException;
import com.sails.client_connect.mapper.CustomerMapper;
import com.sails.client_connect.repository.CustomerRepository;
import com.sails.client_connect.repository.UserRepository;
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
    private final UserRepository userRepository;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper, UserRepository userRepository) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.userRepository=userRepository;
    }

    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Customer customer = customerMapper.toEntity(customerDTO);
        User user = userRepository.findById(customerDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        customer.setUser(user);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toDto(savedCustomer);
    }

    public CustomerDTO getCustomerByIdAndUserId(Long id,Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return customerRepository.findByIdAndUser(id,user)
                .map(customerMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Customer not found with id " + id));
    }

    public List<CustomerDTO> getAllCustomersByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return customerRepository.findAllByUser(user).stream()
                .map(customerMapper::toDto)
                .collect(Collectors.toList());
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

        // Update the user if userId is present in the DTO
        if (customerDTO.getUserId() != null) {
            User user = userRepository.findById(customerDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with id " + customerDTO.getUserId()));
            customer.setUser(user);
        }


        Customer updatedCustomer = customerRepository.save(customer);
        return customerMapper.toDto(updatedCustomer);
    }


    public void deleteCustomer(Long id,Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Customer customer = customerRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Customer not found with id " + id + " for user " + userId));
        customerRepository.delete(customer);
    }

//    public List<CustomerDTO> searchCustomers(String query) {
//        List<Customer> customers = customerRepository.searchCustomers(query);
//        return customers.stream()
//                .map(customerMapper::toDto)
//                .collect(Collectors.toList());
//    }

    public Page<CustomerDTO> searchCustomers(String query, int page, int size, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customerPage = customerRepository.searchCustomersByUser(query, user, pageable);
        return customerPage.map(customerMapper::toDto);
    }

    public Page<CustomerDTO> filterAndSortCustomers(Long id, String firstName, String lastName,
                                                    String email, String phoneNumber, String address,
                                                    int page, int size, Sort sort, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Customer> customerPage = customerRepository.filterAndSortCustomersByUser(
                id, firstName, lastName, email, phoneNumber, address, pageable, user);
        return customerPage.map(customerMapper::toDto);
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
