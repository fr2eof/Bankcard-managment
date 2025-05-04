package com.pet.card_system.core.dto;


public record CardActionRequestProcessDTO(
        boolean approved,
        String adminComment
) {
}
