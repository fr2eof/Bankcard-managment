package com.pet.card_system.api.controller;

import com.pet.card_system.core.dto.AuthResponse;
import com.pet.card_system.core.dto.LoginRequest;
import com.pet.card_system.core.dto.RefreshTokenRequest;
import com.pet.card_system.core.dto.RegisterRequest;
import com.pet.card_system.core.repository.entity.User;
import com.pet.card_system.security.service.AuthService;
import com.pet.card_system.security.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication and authorization")
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;

    @Operation(summary = "Login to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        User user = authService.login(request);
        return ResponseEntity.ok(buildAuthResponse(user));
    }

    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registration successful"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        User user = authService.register(request);
        return ResponseEntity.ok(buildAuthResponse(user));
    }

    @Operation(summary = "Refresh access token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Access token refreshed"),
            @ApiResponse(responseCode = "400", description = "Invalid refresh token")
    })
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
