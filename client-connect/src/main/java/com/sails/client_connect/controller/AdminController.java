package com.sails.client_connect.controller;



import com.sails.client_connect.dto.RoleDTO;
import com.sails.client_connect.dto.RoleUpdateDto;
import com.sails.client_connect.dto.UserAuth;
import com.sails.client_connect.dto.UserRoleUpdateDto;
import com.sails.client_connect.service.RoleService;
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
    @PostMapping("role/addRole")
    public ResponseEntity<RoleDTO> addRole(@RequestBody RoleDTO roleDTO) {
        RoleDTO createdRole = roleService.createRole(roleDTO);
        return new ResponseEntity<>(createdRole, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}/roles")
    public ResponseEntity<UserRoleUpdateDto> updateUserRoles(@PathVariable int userId, @RequestBody Set<RoleUpdateDto> roleUpdateDtos) {
        UserRoleUpdateDto updatedUser = roleService.updateUserRoles(userId, roleUpdateDtos);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("role/delete/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) throws RoleNotFoundException {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("role/{id}")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable Long id) throws RoleNotFoundException {
        RoleDTO roleDTO = roleService.getRoleById(id);
        return ResponseEntity.ok(roleDTO);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        List<RoleDTO> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }
}
