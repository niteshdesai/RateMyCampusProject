package com.ratemycampus.service;

import com.ratemycampus.entity.Department;
import com.ratemycampus.repository.CourseRepository;
import com.ratemycampus.repository.DepartmentRepository;
import com.ratemycampus.repository.StudentRepository;
import com.ratemycampus.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    @Autowired
    private CourseRepository courseRepository;


    public Department saveDepartment(Department department) {
        Optional<Department> existing = departmentRepository
            .findBydeptName(department.getDeptName());

        if (existing.isPresent()) {
            throw new IllegalArgumentException("Department already exists in this college");
        }

        return departmentRepository.save(department);
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public List<Department> getDepartmentsByCollegeId(Long collegeId) {
        return departmentRepository.findByCollegeCid(collegeId);
    }

    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Department not found"));
    }

    public void deleteDepartment(Long id) {

        departmentRepository.deleteById(id);
    }

    public Department updateDepartment(Long id, Department updatedDept) {
            Department dept = getDepartmentById(id);
            dept.setDeptName(updatedDept.getDeptName());
            dept.setDesc(updatedDept.getDesc());
            dept.setCollege(dept.getCollege());
            return departmentRepository.save(dept);
       
    }
    
    public long countStudentsByDepartmentId(Long departmentId) {
        // Verify department exists
        getDepartmentById(departmentId);
        // Count students in this department
        return studentRepository.findByDepartmentDeptId(departmentId).size();
    }
    
    public long countTeachersByDepartmentId(Long departmentId) {
        // Verify department exists
        getDepartmentById(departmentId);
        // Count teachers in this department
        return teacherRepository.findByDepartmentDeptId(departmentId).size();
    }
    
    public long countCoursesByDepartmentId(Long departmentId) {
        // Verify department exists
        getDepartmentById(departmentId);
        // Get all courses in this department
        // Since there's no direct method in CourseRepository, we need to filter courses by department
        return courseRepository.findAll().stream()
                .filter(course -> course.getDepartment() != null && 
                        course.getDepartment().getDeptId().equals(departmentId))
                .count();
    }
}
