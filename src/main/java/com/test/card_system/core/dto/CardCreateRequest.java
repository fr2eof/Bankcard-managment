package com.test.card_system.core.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CardCreateRequest(
        @NotBlank(message = "Encrypted card data cannot be blank")
        @Size(min = 32, max = 512, message = "Invalid encrypted data length")
        String encryptedCardData,

        @NotBlank(message = "Last 4 digits are required")
        @Size(min = 4, max = 4, message = "Must be exactly 4 digits")
        @Pattern(regexp = "\\d{4}", message = "Must contain only digits")
        String lastFourDigits,

        @NotBlank(message = "Card holder name cannot be blank")
        @Size(max = 100, message = "Card holder name must not exceed 100 characters")
        @Pattern(regexp = "^[A-Z ]+$", message = "Card holder name must contain only uppercase letters and spaces")
        String cardHolder,

        @NotNull(message = "Expiry date is required")
        @Future(message = "Expiry date must be in the future")
        LocalDate expiryDate,

        Long userId
) {
}