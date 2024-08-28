package com.sails.client_connect.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponseDto {

    private String accessToken;
    private String token;
    private String message;

    public JwtResponseDto(String message) {
        this.message = message;
    }
}
