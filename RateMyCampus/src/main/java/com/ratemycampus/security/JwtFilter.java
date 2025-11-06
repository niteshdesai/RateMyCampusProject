package com.ratemycampus.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if(jwtUtil.validateToken(token)) {
                String username = jwtUtil.extractUsername(token);
                String role = jwtUtil.extractRole(token);
                Long collegeId = jwtUtil.extractCollegeId(token);
                Long departmentId = jwtUtil.extractDepartmentId(token);

                // Create authentication with college ID and department ID stored in details
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, List.of(() -> "ROLE_" + role));
                
                // Store all claims in details as a map
                java.util.Map<String, Object> details = new java.util.HashMap<>();
                details.put("collegeId", collegeId);
                details.put("departmentId", departmentId);
                details.put("role", role);
                
                // Add HOD-specific details if role is HOD
                if ("HOD".equals(role)) {
                    details.put("hodId", jwtUtil.extractHodId(token));
                    details.put("username", jwtUtil.extractHodUsername(token));
                    details.put("name", jwtUtil.extractName(token));
                    details.put("email", jwtUtil.extractEmail(token));
                    details.put("daImg", jwtUtil.extractDaImg(token));
                }
                
                // Add College Admin-specific details if role is COLLEGE_ADMIN
                if ("COLLEGE_ADMIN".equals(role)) {
                    details.put("adminId", jwtUtil.extractAdminId(token));
                    details.put("name", jwtUtil.extractName(token));
                    details.put("email", jwtUtil.extractEmail(token));
                    details.put("mobile", jwtUtil.extractMobile(token));
                    details.put("imagePath", jwtUtil.extractImagePath(token));
                }
                
                authentication.setDetails(details);
                
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}