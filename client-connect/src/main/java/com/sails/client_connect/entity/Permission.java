//package com.sails.client_connect.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//import jakarta.validation.constraints.NotBlank;
//
//import java.util.Set;
//
//@Entity
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Permission {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @NotBlank(message = "Permission name is required")
//    @Column(unique = true)
//    private String name;
//
//    @ManyToMany(mappedBy = "permissions")
//    private Set<Role> roles;
//}
