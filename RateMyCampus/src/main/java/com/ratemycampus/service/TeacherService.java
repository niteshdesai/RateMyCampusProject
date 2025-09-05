package com.ratemycampus.service;

import com.ratemycampus.entity.*;
import com.ratemycampus.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {

    @Autowired private TeacherRepository teacherRepository;
    @Autowired private CollegeRepository collegeRepository;
    @Autowired private DepartmentRepository departmentRepository;
    // CourseRepository no longer needed; Teacher doesn't directly reference Course

    public Teacher addTeacher(Teacher teacher) {
        validateReferences(teacher);
        return teacherRepository.save(teacher);
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public List<Teacher> getTeachersByCollege(Long collegeId) {
        return teacherRepository.findByCollegeCid(collegeId);
    }

    public List<Teacher> getTeachersByDepartment(Long departmentId) {
        return teacherRepository.findByDepartmentDeptId(departmentId);
    }

    public Teacher getTeacherById(Integer id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found with ID: " + id));
    }

    public Teacher updateTeacher(Integer id, Teacher updated) {
        Teacher existing = getTeacherById(id);
        System.out.println("upadte college"+updated.getCollege());
        validateReferences(updated);

        existing.setTname(updated.getTname());
        existing.setTsem(updated.getTsem());
        existing.setTsection(updated.getTsection());
      
        existing.setTimg(updated.getTimg());
        
        existing.setCollege(updated.getCollege());
        existing.setDepartment(updated.getDepartment());

        return teacherRepository.save(existing);
    }

    public void deleteTeacher(Integer id) {
        if (!teacherRepository.existsByTid(id)) {
            throw new EntityNotFoundException("Teacher not found with ID: " + id);
        }
        teacherRepository.deleteById(id);
    }

    private void validateReferences(Teacher teacher) {
        if (teacher.getCollege() == null || teacher.getCollege().getCid() == null ||
            !collegeRepository.existsById(teacher.getCollege().getCid())) {
        	
            throw new EntityNotFoundException("Invalid College ID");
        }

        if (teacher.getDepartment() == null || teacher.getDepartment().getDeptId() == null ||
            !departmentRepository.existsById(teacher.getDepartment().getDeptId())) {
            throw new EntityNotFoundException("Invalid Department ID");
        }

    }
}
