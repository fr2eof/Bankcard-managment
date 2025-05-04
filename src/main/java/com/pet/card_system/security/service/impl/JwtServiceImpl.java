package com.pet.card_system.security.service.impl;

import com.pet.card_system.core.exception.InvalidTokenTypeException;
import com.pet.card_system.security.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private static final String TOKEN_TYPE_CLAIM = "token_type";
    private static final String ACCESS_TOKEN_TYPE = "access";
    private static final String REFRESH_TOKEN_TYPE = "refresh";

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access-token.expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    @Override
    public String generateAccessToken(Long userId) {
        return generateToken(userId, accessTokenExpiration, ACCESS_TOKEN_TYPE);
    }

    @Override
    public String generateRefreshToken(Long userId) {
        return generateToken(userId, refreshTokenExpiration, REFRESH_TOKEN_TYPE);
    }

    private String generateToken(@NotNull Long userId, long expiration, String tokenType) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim(TOKEN_TYPE_CLAIM, tokenType)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSecretKey())
                .compact();
    }

    private Claims validateAndParseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public String refreshToken(String refreshToken) {
        Claims claims = validateAndParseToken(refreshToken);

        String tokenType = claims.get(TOKEN_TYPE_CLAIM, String.class);
        if (!REFRESH_TOKEN_TYPE.equals(tokenType)) {
            throw new InvalidTokenTypeException("Invalid token type. Refresh token expected.");
        }

        String userId = claims.getSubject();
        return generateAccessToken(Long.valueOf(userId));
    }
}
