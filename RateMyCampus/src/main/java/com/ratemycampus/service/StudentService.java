package com.ratemycampus.service;

import com.ratemycampus.entity.Student;
import com.ratemycampus.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
//import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public Student createStudent(Student student) {
        if (studentRepository.existsByEnrollment(student.getEnrollment())) {
            throw new RuntimeException("Student with this enrollment number already exists");
        }
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
    }

    public Student getByEnrollmentNumber(String enrollmentNumber) {
        return studentRepository.findByEnrollment(enrollmentNumber)
                .orElseThrow(() -> new RuntimeException("Student not found with enrollment number: " + enrollmentNumber));
    }

    public Student updateStudent(Long id, Student updatedStudent) {
        Student existing = getStudentById(id);

        existing.setEnrollment(updatedStudent.getEnrollment());
        existing.setSname(updatedStudent.getSname());
        existing.setSsem(updatedStudent.getSsem());
        existing.setSsection(updatedStudent.getSsection());
        existing.setSgender(updatedStudent.getSgender());
        existing.setSmobile(updatedStudent.getSmobile());
        existing.setScity(updatedStudent.getScity());
        existing.setSimg(updatedStudent.getSimg());
        existing.setSemail(updatedStudent.getSemail());

        existing.setCollege(updatedStudent.getCollege());
        existing.setDepartment(updatedStudent.getDepartment());
        existing.setCourse(updatedStudent.getCourse());

        return studentRepository.save(existing);
    }


    public void deleteStudent(Long id) {
        Student student = getStudentById(id);
        studentRepository.delete(student);
    }

    public List<Student> getStudentsByCollege(Long collegeId) {

        return studentRepository.findByCollegeCid(collegeId);
    }

    public List<Student> getStudentsByDepartment(Long departmentId) {
        return studentRepository.findByDepartmentDeptId(departmentId);
    }

    public List<Student> getStudentsByCourse(Integer courseId) {
        return studentRepository.findByCourseId(courseId);
    }

   
}