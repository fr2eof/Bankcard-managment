package com.pet.card_system.core.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Email не может быть пустым")
        @Email(message = "Некорректный формат email")
        String email,

        @NotBlank(message = "Username cannot be empty")
        @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
        @Pattern(
                regexp = "^[a-zA-Z0-9_]+$",
                message = "Username can only contain letters, numbers and underscores"
        )
        String username,

        @NotBlank(message = "Password cannot be empty")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
                message = "The password must contain uppercase, lowercase letters and numbers."
        )
        String password

) {
}