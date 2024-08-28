package com.sails.client_connect.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpRequestDto {
    private String username;
    private String otp;
}
