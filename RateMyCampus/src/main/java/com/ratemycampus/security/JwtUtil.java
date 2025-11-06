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

    public String generateToken(String username, String role, Long collegeId, Long departmentId) {
        var builder = Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME));
        if (collegeId != null) {
            builder.claim("collegeId", collegeId);
        }
        if (departmentId != null) {
            builder.claim("departmentId", departmentId);
        }
        return builder.signWith(SECRET_KEY).compact();
    }

    // Generate token for HOD with all details
    public String generateTokenForHod(Integer hodId, String username, String name, String email, 
                                      String daImg, Long collegeId, Long departmentId) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", "HOD")
                .claim("hodId", hodId)
                .claim("username", username)
                .claim("name", name)
                .claim("email", email)
                .claim("daImg", daImg)
                .claim("collegeId", collegeId)
                .claim("departmentId", departmentId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    // Generate token for College Admin with all details
    public String generateTokenForCollegeAdmin(Integer adminId, String name, String email, 
                                               String mobile, String imagePath, Long collegeId) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", "COLLEGE_ADMIN")
                .claim("adminId", adminId)
                .claim("name", name)
                .claim("email", email)
                .claim("mobile", mobile)
                .claim("imagePath", imagePath)
                .claim("collegeId", collegeId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
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

    public Long extractDepartmentId(String token) {
        Object val = extractAllClaims(token).get("departmentId");
        if (val == null) return null;
        if (val instanceof Integer) return ((Integer) val).longValue();
        if (val instanceof Long) return (Long) val;
        if (val instanceof String) return Long.parseLong((String) val);
        return null;
    }

    // Extract HOD details
    public Integer extractHodId(String token) {
        Object val = extractAllClaims(token).get("hodId");
        if (val == null) return null;
        if (val instanceof Integer) return (Integer) val;
        if (val instanceof Long) return ((Long) val).intValue();
        if (val instanceof String) return Integer.parseInt((String) val);
        return null;
    }

    public String extractHodUsername(String token) {
        return extractAllClaims(token).get("username", String.class);
    }

    public String extractName(String token) {
        return extractAllClaims(token).get("name", String.class);
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).get("email", String.class);
    }

    public String extractDaImg(String token) {
        return extractAllClaims(token).get("daImg", String.class);
    }

    // Extract College Admin details
    public Integer extractAdminId(String token) {
        Object val = extractAllClaims(token).get("adminId");
        if (val == null) return null;
        if (val instanceof Integer) return (Integer) val;
        if (val instanceof Long) return ((Long) val).intValue();
        if (val instanceof String) return Integer.parseInt((String) val);
        return null;
    }

    public String extractMobile(String token) {
        return extractAllClaims(token).get("mobile", String.class);
    }

    public String extractImagePath(String token) {
        return extractAllClaims(token).get("imagePath", String.class);
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