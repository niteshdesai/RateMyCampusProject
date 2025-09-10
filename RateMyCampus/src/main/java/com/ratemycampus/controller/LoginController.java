package com.ratemycampus.controller;

import com.ratemycampus.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/student")
    public String studentLogin(@RequestParam String enrollment,
                               @RequestParam Integer semester,
                               @RequestParam Long collegeId) {

        String token = loginService.loginStudent(enrollment, collegeId, semester);
        return (token != null) ? token : "INVALID_STUDENT_DETAILS";
    }

    @PostMapping("/hod")
    public String hodLogin(@RequestParam String email,
                           @RequestParam String password) {
        String token = loginService.loginHod(email, password);
        return (token != null) ? token : "INVALID_HOD_CREDENTIALS";
    }

    @PostMapping("/college-admin")
    public String collegeAdminLogin(@RequestParam String email,
                                    @RequestParam String password) {
        String token = loginService.loginCollegeAdmin(email, password);
        return (token != null) ? token : "INVALID_COLLEGE_ADMIN_CREDENTIALS";
    }

    @PostMapping("/admin")
    public String adminLogin(@RequestParam String email,
                             @RequestParam String password) {
        String token = loginService.loginAdmin(email, password);
        return (token != null) ? token : "INVALID_ADMIN_CREDENTIALS";
    }
}