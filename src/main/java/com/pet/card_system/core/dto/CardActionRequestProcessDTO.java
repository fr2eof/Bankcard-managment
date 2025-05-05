package com.pet.card_system.core.dto;


import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CardActionRequestProcessDTO(
        boolean approved,

        @Size(max = 500, message = "Admin comment should not exceed 500 characters")
        @Pattern(regexp = "^[a-zA-Z0-9,\\.\\-\\s]*$", message = "Admin comment can only contain alphanumeric characters, spaces, commas, dots, and hyphens")
        String adminComment
) {
}
