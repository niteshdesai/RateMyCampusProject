package com.ratemycampus.service;

import com.ratemycampus.entity.Student;
import com.ratemycampus.entity.DepartmentAdmin;
import com.ratemycampus.entity.CollegeAdmin;
import com.ratemycampus.entity.Admin;
import com.ratemycampus.repository.StudentRepository;
import com.ratemycampus.repository.DepartmentAdminRepository;
import com.ratemycampus.repository.CollegeAdminRepository;
import com.ratemycampus.repository.AdminRepository;
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
    private AdminRepository adminRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public String loginStudent(String enrollment, Long collegeId, Integer semester) {
        Student student = studentRepo.findByEnrollmentAndCollegeCidAndSsem(enrollment, collegeId, semester);
        System.out.println(student);
        if (student != null) {
            return jwtUtil.generateTokenForStudent(
                    student.getSid(),
                    student.getEnrollment(),
                    student.getSname(),
                    student.getSsem(),
                    student.getSsection(),
                    student.getSgender(),
                    student.getSmobile(),
                    student.getScity(),
                    student.getSimg(),
                    student.getSemail(),
                    student.getCollege() != null ? student.getCollege().getCid() : collegeId,
                    student.getDepartment() != null ? student.getDepartment().getDeptId() : null,
                    student.getCourse() != null ? student.getCourse().getC_id() : null);
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
                    hod.getDepartment().getDeptId());
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
                    collegeAdmin.getCollege().getCid());
        }
        return null;
    }

    public String loginAdmin(String email, String password) {
        Admin admin = adminRepository.findByEmailAndPassword(email, password);
        System.out.println(admin.toString());
        if (admin != null) {
            return jwtUtil.generateTokenForPlatformAdmin(
                    admin.getId(),
                    admin.getName(),
                    admin.getEmail());
                   
        }
        return null;
    }
}