package com.test.card_system.security.service.impl;

import com.test.card_system.core.dto.LoginRequest;
import com.test.card_system.core.dto.RegisterRequest;
import com.test.card_system.core.exception.UserAlreadyExistsException;
import com.test.card_system.core.exception.UserNotFoundException;
import com.test.card_system.core.repository.UserRepository;
import com.test.card_system.core.repository.entity.User;
import com.test.card_system.security.service.AuthService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User register(@NotNull RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.email())) {
            throw new UserAlreadyExistsException("Email already in use");
        }
        User user = new User();
        user.setUsername(registerRequest.username());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.setEmail(registerRequest.email());

        return userRepository.save(user);
    }


    @Override
    @Transactional(readOnly = true)
    public User login(@NotNull LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        return user;
    }
}