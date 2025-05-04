package com.pet.card_system.security.service;

import com.pet.card_system.core.dto.LoginRequest;
import com.pet.card_system.core.dto.RegisterRequest;
import com.pet.card_system.core.repository.entity.User;
import jakarta.validation.Valid;

public interface AuthService {
    User login(@Valid LoginRequest request);

    User register(@Valid RegisterRequest request);

}
