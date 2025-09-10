package com.ratemycampus.service;

import com.ratemycampus.entity.Student;
import com.ratemycampus.entity.DepartmentAdmin;
import com.ratemycampus.entity.CollegeAdmin;
import com.ratemycampus.repository.StudentRepository;
import com.ratemycampus.repository.DepartmentAdminRepository;
import com.ratemycampus.repository.CollegeAdminRepository;
import com.ratemycampus.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private DepartmentAdminRepository deptAdminRepo;

    @Autowired
    private CollegeAdminRepository collegeAdminRepo;

    @Autowired
    private JwtUtil jwtUtil;

    // Static admin credentials
    private static final String ADMIN_EMAIL = "admin@ratemycampus.com";
    private static final String ADMIN_PASSWORD = "admin123";

    public String loginStudent(String enrollment, Long collegeId, Integer semester) {
        Student student = studentRepo.findByEnrollmentAndCollegeCidAndSsem(enrollment, collegeId, semester);

        if (student != null) {
            // Always store the provided collegeId in the JWT
            String JWTtoken= jwtUtil.generateToken(student.getEnrollment(), "STUDENT", collegeId);
            System.out.println(JWTtoken);
            return JWTtoken;
        }
        return null;
    }

    public String loginHod(String email, String password) {
        DepartmentAdmin hod = deptAdminRepo.findByEmailAndPassword(email, password);
        if (hod != null && hod.getCollege().getCid() != null) {
            return jwtUtil.generateToken(hod.getEmail(), "HOD",hod.getCollege().getCid());
        }
        return null;
    }

    public String loginCollegeAdmin(String email, String password) {
        CollegeAdmin collegeAdmin = collegeAdminRepo.findByEmailAndPassword(email, password);
        if (collegeAdmin != null && collegeAdmin.getCollege().getCid()!= null) {
            return jwtUtil.generateToken(collegeAdmin.getEmail(), "COLLEGE_ADMIN", collegeAdmin.getCollege().getCid());
        }
        return null;
    }

    public String loginAdmin(String email, String password) {
        if (ADMIN_EMAIL.equals(email) && ADMIN_PASSWORD.equals(password)) {
            return jwtUtil.generateToken(email, "ADMIN", null); // No collegeId for admin
        }
        return null;
    }
}