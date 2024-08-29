package com.sails.client_connect.controller;



import com.sails.client_connect.dto.*;
import com.sails.client_connect.service.CustomerService;
import com.sails.client_connect.service.RoleService;
import com.sails.client_connect.service.TaskService;
import com.sails.client_connect.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final CustomerService customerService;
    private final TaskService taskService;
    private final RoleService roleService;

    @PostMapping("/adduser")
    public ResponseEntity<String> addUser(@RequestBody UserAuth userAuth){
        try{
            userService.saveUser(userAuth);
            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully" );
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PostMapping("/role/addRole")
    public ResponseEntity<RoleDTO> addRole(@RequestBody RoleDTO roleDTO) {
        RoleDTO createdRole = roleService.createRole(roleDTO);
        return new ResponseEntity<>(createdRole, HttpStatus.CREATED);
    }

    @PutMapping("/roles/{userId}")
    public ResponseEntity<UserRoleUpdateDTO> updateUserRoles(@PathVariable int userId, @RequestBody Set<RoleUpdateDTO> roleUpdateDtos) {
        UserRoleUpdateDTO updatedUser = roleService.updateUserRoles(userId, roleUpdateDtos);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/role/delete/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) throws RoleNotFoundException {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/role/{id}")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable Long id) throws RoleNotFoundException {
        RoleDTO roleDTO = roleService.getRoleById(id);
        return ResponseEntity.ok(roleDTO);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        List<RoleDTO> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.findAllUsers();
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

    @GetMapping("/customers/names")
    public ResponseEntity<List<CustomersFinancingDto>> getCustomersNames() {
        List<CustomersFinancingDto> customers = customerService.getCustomersNames();
        return ResponseEntity.ok(customers);
    }
}
