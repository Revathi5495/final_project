//package com.sails.client_connect.entity;
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Pattern;
//import jakarta.validation.constraints.Size;
//import lombok.*;
//import java.util.Set;
//
//@Entity
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Admin {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @NotBlank(message = "First Name is required")
//    private String firstName;
//
//    @NotBlank(message = "Lastname is required")
//    private String lastName;
//
//    @NotBlank(message = "Email is required")
//    private String email;
//
//    @NotBlank(message = "Password is required")
//    private String password;
//
//    @NotBlank(message = "Phone number is required")
//    @Size(min=10,max=10)
//    @Pattern(regexp="\\d{10}", message="phone number must contain digits only")
//    private String phoneNumber;
//
//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "user_roles",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id")
//    )
//    private Set<Role> roles;
//}