package com.sails.client_connect.controller;


import com.sails.client_connect.dto.UserAuth;
import com.sails.client_connect.dto.UserAuthRequest;
import com.sails.client_connect.dto.UserDto;
import com.sails.client_connect.service.JwtService;
import com.sails.client_connect.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @PostMapping("/admin/adduser")
    public ResponseEntity<String> addUser(@RequestBody UserAuth userAuth){
        try{
            userService.saveUser(userAuth);
            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully" );
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

//    @PostMapping("/admin/api")
//    public void addUser(@RequestBody UserAuth userAuth) {
//        userService.saveUser(userAuth);
//    }

    @GetMapping("/all")
    public List<UserDto> getAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/user/process")
    //@PreAuthorize("hasRole('USER')")
    public String process() {
        return "processing..";
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }



    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody UserAuthRequest userAuthRequest) {
        Authentication authenticate = authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(userAuthRequest.getUsername(), userAuthRequest.getPassword()));

        if(authenticate.isAuthenticated()) {
            return jwtService.generateToken(userAuthRequest.getUsername());
        }
        else{
            throw new UsernameNotFoundException("invalid user request !");
        }

    }


    @PutMapping("/user/update-password")
    public ResponseEntity<String> updatePassword(@RequestParam String username, @RequestParam String newPassword) {
        userService.updatePassword(username, newPassword);
        return ResponseEntity.ok("Password updated successfully.");
    }


}
