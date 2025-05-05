package com.pet.card_system.core.dto;


import com.pet.card_system.core.repository.entity.Role;

public record UserDTO(
        Long id,
        String username,
        String email,
        Role role
) {
}
