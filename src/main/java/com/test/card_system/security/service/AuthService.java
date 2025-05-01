package com.test.card_system.security.service;

import com.test.card_system.core.dto.LoginRequest;
import com.test.card_system.core.dto.RegisterRequest;
import com.test.card_system.core.repository.entity.User;
import jakarta.validation.Valid;

public interface AuthService {
    User login(@Valid LoginRequest request);

    User register(@Valid RegisterRequest request);

}
