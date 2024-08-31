package com.sails.client_connect.controller;


import com.sails.client_connect.dto.*;
import com.sails.client_connect.entity.RefreshToken;
import com.sails.client_connect.entity.User;
import com.sails.client_connect.service.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final EmailService emailService;
    private final OtpService otpService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;


    private Map<String, String> otpStore = new HashMap<>();
    private Map<String, Long> otpExpiryStore = new HashMap<>();

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserAuthRequest userAuthRequest) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userAuthRequest.getUsername(),
                        userAuthRequest.getPassword()
                )
        );

        if (!authenticate.isAuthenticated()) {
            throw new UsernameNotFoundException("Invalid user credentials!");
        }

        String otp = otpService.generateOtp();
        otpStore.put(userAuthRequest.getUsername(), otp);
        otpExpiryStore.put(userAuthRequest.getUsername(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5));

        User user = userService.findByUsername(userAuthRequest.getUsername());
        emailService.sendOtp(user.getEmail(), otp);

        return ResponseEntity.ok("OTP has been sent to your email.");
    }

    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@Valid @RequestBody UserAuthRequest userAuthRequest, HttpSession session) {
        Authentication authenticate = authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(userAuthRequest.getUsername(), userAuthRequest.getPassword()));

        if(authenticate.isAuthenticated()) {
            // Get the authenticated user
            User authenticatedUser = userService.findByUsername(userAuthRequest.getUsername());

            // Store user ID in session
            session.setAttribute("userId", authenticatedUser.getUser_id());
            return jwtService.generateToken(userAuthRequest.getUsername());
        }
        else{
            throw new UsernameNotFoundException("invalid user request !");
        }

    }


    @PostMapping("/verify-otp")
    public ResponseEntity<JwtResponseDTO> verifyOtp(@Valid @RequestBody OtpRequestDTO otpRequestDto) {

        if (!otpStore.containsKey(otpRequestDto.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JwtResponseDTO("OTP not found for this user."));
        }

        Long expiryTime = otpExpiryStore.get(otpRequestDto.getUsername());
        if (expiryTime < System.currentTimeMillis()) {
            otpStore.remove(otpRequestDto.getUsername());
            otpExpiryStore.remove(otpRequestDto.getUsername());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JwtResponseDTO("OTP has expired."));
        }


        String storedOtp = otpStore.get(otpRequestDto.getUsername());
        if (!storedOtp.equals(otpRequestDto.getOtp())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JwtResponseDTO("Invalid OTP."));
        }

        otpStore.remove(otpRequestDto.getUsername());
        otpExpiryStore.remove(otpRequestDto.getUsername());

        String jwt = jwtService.generateToken(otpRequestDto.getUsername());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(otpRequestDto.getUsername());

        JwtResponseDTO responseDto = JwtResponseDTO.builder()
                                                    .accessToken(jwt)
                                                    .refreshToken(refreshToken.getToken())
                                                    .message("Access Token and Refresh Token are created")
                                                    .build();

        return ResponseEntity.ok(responseDto);
    }


    @PostMapping("/refreshToken")
    public ResponseEntity<JwtResponseDTO> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return refreshTokenService.findByToken(refreshTokenRequest.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {

                    //generate a new token with username
                    String accessToken = jwtService.generateToken(user.getUsername());

                    JwtResponseDTO responseDto = JwtResponseDTO.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshTokenRequest.getToken())
                            .message("New Access Token and Refresh Token are created.")
                            .build();

                    return ResponseEntity.ok(responseDto);
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestParam String username, @RequestParam String newPassword) {
        userService.updatePassword(username, newPassword);
        return ResponseEntity.ok("Password changed successfully");
    }

    @PostMapping("/adduser")
    public ResponseEntity<String> addUser(@Valid @RequestBody UserAuth userAuth){
        try{
            userService.saveUser(userAuth);
            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully" );
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


}
