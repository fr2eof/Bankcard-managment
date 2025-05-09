package com.pet.card_system.core.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
}
