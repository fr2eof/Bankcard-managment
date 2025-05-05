package com.pet.card_system.core.dto;

import jakarta.validation.constraints.*;

public record UserUpdateRequest(

        @Email(message = "Email must be valid")
        @Size(max = 100, message = "Email must not exceed 100 characters")
        @NotNull
        String email,

        @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
        @Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "Username must contain only letters, digits, and underscores")
        String username,

        String password,

        @Pattern(regexp = "^(USER|ADMIN)?$", message = "Role must be USER or ADMIN")
        String role
) {
}
