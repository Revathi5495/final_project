package com.sails.client_connect.controller;

import com.sails.client_connect.dto.CustomerDTO;
import com.sails.client_connect.dto.RoleDto;
import com.sails.client_connect.dto.TaskDTO;
import com.sails.client_connect.dto.UserDto;
import com.sails.client_connect.dto.UserAuth;
import com.sails.client_connect.service.RoleService;
import com.sails.client_connect.service.TaskService;
import com.sails.client_connect.service.UserService;
import com.sails.client_connect.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final CustomerService customerService;
    private final TaskService taskService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDTO>> getAllTasksToAdminView() {
        List<TaskDTO> tasks = taskService.getAllTasksToAdminView();
        return ResponseEntity.ok(tasks);
    }


    @PostMapping("admin/adduser")
    public ResponseEntity<String> addUser(@RequestBody UserAuth userAuth){
        try{
            userService.saveUser(userAuth);
            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully" );
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PostMapping("role/addRole")
    public ResponseEntity<RoleDto> addRole(@RequestBody RoleDto roleDTO) {
        System.out.println("Role accessed successfully:");
        RoleDto createdRole = roleService.createRole(roleDTO);
        System.out.println("Role created successfully:");
        return new ResponseEntity<>(createdRole, HttpStatus.CREATED);
    }

    @PutMapping("role/{id}")
    public ResponseEntity<RoleDto> updateRole(@PathVariable Long id, @RequestBody RoleDto roleDTO) {
        RoleDto updatedRole = roleService.updateRole(id, roleDTO);
        return ResponseEntity.ok(updatedRole);
    }

//    @DeleteMapping("role/delete/{id}")
//    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
//        roleService.deleteRole(id);
//        return ResponseEntity.noContent().build();
//    }

    @GetMapping("role/{id}")
    public ResponseEntity<RoleDto> getRoleById(@PathVariable Long id) {
        RoleDto roleDTO = roleService.getRoleById(id);
        return ResponseEntity.ok(roleDTO);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        List<RoleDto> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }
}
