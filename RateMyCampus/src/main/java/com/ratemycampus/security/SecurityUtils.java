package com.ratemycampus.security;

import com.ratemycampus.entity.CollegeAdmin;
import com.ratemycampus.entity.DepartmentAdmin;
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
                java.util.Map<String, Object> details = (java.util.Map<String, Object>) authentication.getDetails();
                Object collegeId = details.get("collegeId");
                if (collegeId instanceof Long) return (Long) collegeId;
                if (collegeId instanceof Integer) return ((Integer) collegeId).longValue();
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
                java.util.Map<String, Object> details = (java.util.Map<String, Object>) authentication.getDetails();
                Object departmentId = details.get("departmentId");
                if (departmentId instanceof Long) return (Long) departmentId;
                if (departmentId instanceof Integer) return ((Integer) departmentId).longValue();
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

    /**
     * Get current HOD from JWT token details
     * @return DepartmentAdmin object with JWT details, null if not HOD
     */
    public DepartmentAdmin getCurrentHod() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getDetails() instanceof java.util.Map) {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> details = (java.util.Map<String, Object>) authentication.getDetails();
                
                if ("HOD".equals(details.get("role"))) {
                    DepartmentAdmin hod = new DepartmentAdmin();
                    
                    Object hodId = details.get("hodId");
                    if (hodId instanceof Integer) hod.setHodId((Integer) hodId);
                    else if (hodId instanceof Long) hod.setHodId(((Long) hodId).intValue());
                    
                    hod.setUsername((String) details.get("username"));
                    hod.setName((String) details.get("name"));
                    hod.setEmail((String) details.get("email"));
                    hod.setDaImg((String) details.get("daImg"));
                    
                    return hod;
                }
            }
        } catch (Exception e) {
            // Log error if needed
        }
        return null;
    }

    /**
     * Get current College Admin from JWT token details
     * @return CollegeAdmin object with JWT details, null if not College Admin
     */
    public CollegeAdmin getCurrentCollegeAdmin() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getDetails() instanceof java.util.Map) {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> details = (java.util.Map<String, Object>) authentication.getDetails();
                
                if ("COLLEGE_ADMIN".equals(details.get("role"))) {
                    CollegeAdmin admin = new CollegeAdmin();
                    
                    Object adminId = details.get("adminId");
                    if (adminId instanceof Integer) admin.setId((Integer) adminId);
                    else if (adminId instanceof Long) admin.setId(((Long) adminId).intValue());
                    
                    admin.setName((String) details.get("name"));
                    admin.setEmail((String) details.get("email"));
                    admin.setMobile((String) details.get("mobile"));
                    admin.setImagePath((String) details.get("imagePath"));
                    
                    return admin;
                }
            }
        } catch (Exception e) {
            // Log error if needed
        }
        return null;
    }
}
