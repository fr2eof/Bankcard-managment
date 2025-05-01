package com.test.card_system.security.service;


public interface JwtService {
    String refreshToken(String request);

    String generateAccessToken(Long user);

    String generateRefreshToken(Long user);

}
