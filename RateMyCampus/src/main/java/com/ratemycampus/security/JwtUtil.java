package com.ratemycampus.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Generate a secure key with at least 256 bits
    private final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

    public String generateToken(String username, String role) {
        return generateToken(username, role, null);
    }

    public String generateToken(String username, String role, Long collegeId) {
        var builder = Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME));
        if (collegeId != null) {
            builder.claim("collegeId", collegeId);
        }
        return builder.signWith(SECRET_KEY).compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    public Long extractCollegeId(String token) {
        Object val = extractAllClaims(token).get("collegeId");
        if (val == null) return null;
        if (val instanceof Integer) return ((Integer) val).longValue();
        if (val instanceof Long) return (Long) val;
        if (val instanceof String) return Long.parseLong((String) val);
        return null;
    }

    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private io.jsonwebtoken.Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}