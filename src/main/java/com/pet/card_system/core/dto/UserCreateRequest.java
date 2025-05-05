package com.pet.card_system.core.dto;

import jakarta.validation.constraints.*;

public record UserCreateRequest(

        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username must contain only letters, digits, and underscores")
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,

        @NotBlank(message = "Password is required")
        String password,

        @NotBlank(message = "Role is required")
        @Pattern(regexp = "^(USER|ADMIN)$", message = "Role must be USER or ADMIN")
        String role
) {
}
