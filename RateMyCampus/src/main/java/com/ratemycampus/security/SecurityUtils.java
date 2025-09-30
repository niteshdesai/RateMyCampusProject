package com.ratemycampus.security;

import com.ratemycampus.entity.Student;
import com.ratemycampus.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityUtils {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private StudentRepository studentRepository;

    /**
     * Extract college ID from JWT token in the current request
     * @return College ID from JWT token, null if not found or invalid
     */
    public Long getCurrentUserCollegeId() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getDetails() instanceof java.util.Map) {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Long> details = (java.util.Map<String, Long>) authentication.getDetails();
                return details.get("collegeId");
            }
        } catch (Exception e) {
            // Log error if needed
        }
        return null;
    }

    /**
     * Extract department ID from JWT token in the current request
     * @return Department ID from JWT token, null if not found or invalid
     */
    public Long getCurrentUserDepartmentId() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getDetails() instanceof java.util.Map) {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Long> details = (java.util.Map<String, Long>) authentication.getDetails();
                return details.get("departmentId");
            }
        } catch (Exception e) {
            // Log error if needed
        }
        return null;
    }

    /**
     * Extract college ID from JWT token string
     * @param token JWT token string
     * @return College ID from JWT token, null if not found or invalid
     */
    public Long getCollegeIdFromToken(String token) {
        try {
            if (jwtUtil.validateToken(token)) {
                return jwtUtil.extractCollegeId(token);
            }
        } catch (Exception e) {
            // Log error if needed
        }
        return null;
    }

    /**
     * Get current user's role from JWT token
     * @return Role from JWT token, null if not found or invalid
     */
    public String getCurrentUserRole() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
                return authentication.getAuthorities().iterator().next().getAuthority();
            }
        } catch (Exception e) {
            // Log error if needed
        }
        return null;
    }

    /**
     * Get current student's ID from JWT token
     * @return Student ID from JWT token, null if not found or invalid
     */
    public Integer getCurrentStudentId() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getName() != null) {
                String enrollment = authentication.getName();
                Optional<Student> student = studentRepository.findByEnrollment(enrollment);
                if (student.isPresent()) {
                    return student.get().getSid();
                }
            }
        } catch (Exception e) {
            // Log error if needed
        }
        return null;
    }

    /**
     * Get current student's enrollment from JWT token
     * @return Enrollment from JWT token, null if not found or invalid
     */
    public String getCurrentStudentEnrollment() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getName() != null) {
                return authentication.getName();
            }
        } catch (Exception e) {
            // Log error if needed
        }
        return null;
    }
}
