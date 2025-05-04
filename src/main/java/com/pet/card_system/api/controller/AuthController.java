package com.pet.card_system.api.controller;

import com.pet.card_system.core.dto.AuthResponse;
import com.pet.card_system.core.dto.LoginRequest;
import com.pet.card_system.core.dto.RefreshTokenRequest;
import com.pet.card_system.core.dto.RegisterRequest;
import com.pet.card_system.core.repository.entity.User;
import com.pet.card_system.security.service.AuthService;
import com.pet.card_system.security.service.JwtService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        User user = authService.login(request);

        return ResponseEntity.ok(buildAuthResponse(user));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        User user = authService.register(request);

        return ResponseEntity.ok(buildAuthResponse(user));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        String newAccessToken = jwtService.refreshToken(request.refreshToken());

        return ResponseEntity.ok(AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(request.refreshToken())
                .build());
    }

    private AuthResponse buildAuthResponse(@NotNull User user) {
        return AuthResponse.builder()
                .accessToken(jwtService.generateAccessToken(user.getId()))
                .refreshToken(jwtService.generateRefreshToken(user.getId()))
                .build();
    }
}