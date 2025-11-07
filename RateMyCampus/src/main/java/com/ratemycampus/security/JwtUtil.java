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

    // Generate token for Student with all details
    public String generateTokenForStudent(Integer sid, String enrollment, String sname, 
                                          Integer ssem, String ssection, String sgender, 
                                          String smobile, String scity, String simg, 
                                          String semail, Long collegeId, Long departmentId, Integer courseId) {
        var builder = Jwts.builder()
                .setSubject(enrollment)
                .claim("role", "STUDENT")
                .claim("sid", sid)
                .claim("enrollment", enrollment)
                .claim("sname", sname)
                .claim("ssem", ssem)
                .claim("ssection", ssection)
                .claim("sgender", sgender)
                .claim("smobile", smobile)
                .claim("scity", scity)
                .claim("semail", semail)
                .claim("collegeId", collegeId)
                .claim("departmentId", departmentId)
                .claim("courseId", courseId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME));
        
        if (simg != null) {
            builder.claim("simg", simg);
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

    // Extract Student details
    public Integer extractSid(String token) {
        Object val = extractAllClaims(token).get("sid");
        if (val == null) return null;
        if (val instanceof Integer) return (Integer) val;
        if (val instanceof Long) return ((Long) val).intValue();
        if (val instanceof String) return Integer.parseInt((String) val);
        return null;
    }

    public String extractEnrollment(String token) {
        return extractAllClaims(token).get("enrollment", String.class);
    }

    public String extractSname(String token) {
        return extractAllClaims(token).get("sname", String.class);
    }

    public Integer extractSsem(String token) {
        Object val = extractAllClaims(token).get("ssem");
        if (val == null) return null;
        if (val instanceof Integer) return (Integer) val;
        if (val instanceof Long) return ((Long) val).intValue();
        if (val instanceof String) return Integer.parseInt((String) val);
        return null;
    }

    public String extractSsection(String token) {
        return extractAllClaims(token).get("ssection", String.class);
    }

    public String extractSgender(String token) {
        return extractAllClaims(token).get("sgender", String.class);
    }

    public String extractSmobile(String token) {
        return extractAllClaims(token).get("smobile", String.class);
    }

    public String extractScity(String token) {
        return extractAllClaims(token).get("scity", String.class);
    }

    public String extractSimg(String token) {
        return extractAllClaims(token).get("simg", String.class);
    }

    public String extractSemail(String token) {
        return extractAllClaims(token).get("semail", String.class);
    }

    public Integer extractCourseId(String token) {
        Object val = extractAllClaims(token).get("courseId");
        if (val == null) return null;
        if (val instanceof Integer) return (Integer) val;
        if (val instanceof Long) return ((Long) val).intValue();
        if (val instanceof String) return Integer.parseInt((String) val);
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