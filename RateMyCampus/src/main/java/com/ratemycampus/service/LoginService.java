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

            return JWTtoken;
        }
        return null;
    }

    public String loginHod(String email, String password) {
        DepartmentAdmin hod = deptAdminRepo.findByEmailAndPassword(email, password);
        if (hod != null && hod.getCollege() != null && hod.getDepartment() != null) {
            return jwtUtil.generateTokenForHod(
                hod.getHodId(),
                hod.getUsername(),
                hod.getName(),
                hod.getEmail(),
                hod.getDaImg(),
                hod.getCollege().getCid(),
                hod.getDepartment().getDeptId()
            );
        }
        return null;
    }

    public String loginCollegeAdmin(String email, String password) {
        CollegeAdmin collegeAdmin = collegeAdminRepo.findByEmailAndPassword(email, password);
        if (collegeAdmin != null && collegeAdmin.getCollege() != null) {
            return jwtUtil.generateTokenForCollegeAdmin(
                collegeAdmin.getId(),
                collegeAdmin.getName(),
                collegeAdmin.getEmail(),
                collegeAdmin.getMobile(),
                collegeAdmin.getImagePath(),
                collegeAdmin.getCollege().getCid()
            );
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