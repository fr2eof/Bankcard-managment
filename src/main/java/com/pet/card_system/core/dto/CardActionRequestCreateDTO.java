package com.pet.card_system.core.dto;

import com.pet.card_system.core.repository.entity.CardActionType;

import jakarta.validation.constraints.*;

public record CardActionRequestCreateDTO(

        @Size(max = 500, message = "User comment must not exceed 500 characters")
        @Pattern(
                regexp = "^[a-zA-Zа-яА-Я0-9 ,\\.\\-!?()\"'\\n\\r]*$",
                message = "User comment contains invalid characters"
        )
        String userComment,

        @NotNull(message = "Action type must be specified")
        CardActionType action
) {
}

