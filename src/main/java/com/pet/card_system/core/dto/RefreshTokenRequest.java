package com.pet.card_system.core.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
        @NotBlank(message = "Refresh token cannot be empty")
        String refreshToken
) {}
