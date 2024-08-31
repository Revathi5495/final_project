package com.sails.client_connect.controller;


import com.sails.client_connect.config.CustomUserDetails;
import com.sails.client_connect.dto.UserDTO;
import com.sails.client_connect.service.JwtService;
import com.sails.client_connect.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @GetMapping("/all")
    public List<UserDTO> getAllUsers() {
        return userService.findAllUsers();
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }



    @PutMapping("/update-password")
    public ResponseEntity<String> updatePassword(
            @RequestParam String username,
            @RequestParam String newPassword,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        String loggedInUsername = customUserDetails.getUsername();

        if (!loggedInUsername.equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: Invalid token for the requested user.");
        }


        userService.updatePassword(username, newPassword);
        return ResponseEntity.ok("Password updated successfully.");
    }


}
