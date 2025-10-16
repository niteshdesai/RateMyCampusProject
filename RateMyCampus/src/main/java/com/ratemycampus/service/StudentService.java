package com.ratemycampus.service;

import com.ratemycampus.entity.Student;
import com.ratemycampus.repository.CollegeRepository;
import com.ratemycampus.repository.DepartmentRepository;
import com.ratemycampus.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
//import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CollegeRepository collegeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    public Student createStudent(Student student) {
        if (studentRepository.existsByEnrollment(student.getEnrollment())) {
            throw new RuntimeException("Student with this enrollment number already exists: " + student.getEnrollment());
        }

        validateReferences(student);
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

        validateReferences(updatedStudent);

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

    private void validateReferences(Student student) {
        if (student.getCollege() == null || student.getCollege().getCid() == null ||
            !collegeRepository.existsById(student.getCollege().getCid())) {
            Long provided = student.getCollege() != null ? student.getCollege().getCid() : null;
            throw new RuntimeException("Invalid College ID. collegeId=" + provided);
        }
        if (student.getDepartment() == null || student.getDepartment().getDeptId() == null ||
            !departmentRepository.existsById(student.getDepartment().getDeptId())) {
            Long provided = student.getDepartment() != null ? student.getDepartment().getDeptId() : null;
            throw new RuntimeException("Invalid Department ID. departmentId=" + provided);
        }
        Long collegeId = student.getCollege().getCid();
        Long deptId = student.getDepartment().getDeptId();
        if (!departmentRepository.existsByDeptIdAndCollegeCid(deptId, collegeId)) {
            throw new RuntimeException(
                "Department does not belong to the provided college. departmentId=" + deptId + ", collegeId=" + collegeId
            );
        }
    }
}