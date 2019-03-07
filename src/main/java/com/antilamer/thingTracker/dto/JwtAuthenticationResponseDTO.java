package com.antilamer.thingTracker.dto;

import lombok.Data;

@Data
public class JwtAuthenticationResponseDTO {

    private String accessToken;
    private String tokenType = "Bearer";

    public JwtAuthenticationResponseDTO(String accessToken) {
        this.accessToken = accessToken;
    }

}
