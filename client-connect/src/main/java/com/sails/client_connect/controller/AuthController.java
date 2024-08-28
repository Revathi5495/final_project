package com.sails.client_connect.controller;


import com.sails.client_connect.dto.JwtResponseDto;
import com.sails.client_connect.dto.OtpRequestDto;
import com.sails.client_connect.dto.RefreshTokenRequest;
import com.sails.client_connect.dto.UserAuthRequest;
import com.sails.client_connect.entity.RefreshToken;
import com.sails.client_connect.entity.User;
import com.sails.client_connect.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<String> login(@RequestBody UserAuthRequest userAuthRequest) {
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
        otpExpiryStore.put(userAuthRequest.getUsername(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(1));

        User user = userService.findByUsername(userAuthRequest.getUsername());
        emailService.sendOtp(user.getEmail(), otp);

        return ResponseEntity.ok("OTP has been sent to your email.");
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<JwtResponseDto> verifyOtp(@RequestBody OtpRequestDto otpRequestDto) {

        if (!otpStore.containsKey(otpRequestDto.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JwtResponseDto("OTP not found for this user."));
        }

        Long expiryTime = otpExpiryStore.get(otpRequestDto.getUsername());
        if (expiryTime < System.currentTimeMillis()) {
            otpStore.remove(otpRequestDto.getUsername());
            otpExpiryStore.remove(otpRequestDto.getUsername());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JwtResponseDto("OTP has expired."));
        }


        String storedOtp = otpStore.get(otpRequestDto.getUsername());
        if (!storedOtp.equals(otpRequestDto.getOtp())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JwtResponseDto("Invalid OTP."));
        }

        otpStore.remove(otpRequestDto.getUsername());
        otpExpiryStore.remove(otpRequestDto.getUsername());

        String jwt = jwtService.generateToken(otpRequestDto.getUsername());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(otpRequestDto.getUsername());

        JwtResponseDto responseDto = JwtResponseDto.builder()
                                                    .accessToken(jwt)
                                                    .token(refreshToken.getToken())
                                                    .build();

        return ResponseEntity.ok(responseDto);
    }


    @PostMapping("/refreshToken")
    public ResponseEntity<JwtResponseDto> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return refreshTokenService.findByToken(refreshTokenRequest.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {

                    //generate a new token with username
                    String accessToken = jwtService.generateToken(user.getUsername());

                    JwtResponseDto responseDto = JwtResponseDto.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequest.getToken())
                            .build();

                    return ResponseEntity.ok(responseDto);
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }


}
