package com.pet.card_system.core.dto;

import com.pet.card_system.core.repository.entity.CardActionType;

public record CardActionRequestCreateDTO(
        String userComment,
        CardActionType action
) {
}
