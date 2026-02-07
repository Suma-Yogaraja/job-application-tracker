package com.jobtracker.job_application_tracker.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final Key signingKey;
    private final long expirationMinutes;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expirationMinutes}") long expirationMinutes
    ) {
        if (secret == null || secret.length() < 32) {
            throw new IllegalArgumentException("jwt.secret must be at least 32 characters long");
        }
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMinutes = expirationMinutes;
    }

    public String generateToken(String email) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(expirationMinutes * 60);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(signingKey)
                .compact();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, String expectedEmail) {
        try {
            Claims claims = extractAllClaims(token);
            String subject = claims.getSubject();
            Date expiration = claims.getExpiration();
            return subject != null
                    && subject.equals(expectedEmail)
                    && expiration != null
                    && expiration.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
