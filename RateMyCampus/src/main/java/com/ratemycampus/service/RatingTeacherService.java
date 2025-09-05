package com.ratemycampus.service;

import com.ratemycampus.entity.*;
import com.ratemycampus.repository.RatingTeacherRepository;
import com.ratemycampus.repository.StudentRepository;
import com.ratemycampus.repository.TeacherRepository;
import com.ratemycampus.repository.TeacherCourseRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ratemycampus.entity.RatingTeacher;

import java.util.List;

@Service
public class RatingTeacherService {

    @Autowired
    private RatingTeacherRepository ratingTeacherRepository ;
    
    @Autowired
    private TeacherRepository teacheRepository;
    
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherCourseRepository teacherCourseRepository;

    public RatingTeacher addRating(RatingTeacher rating) {
        if (rating.getTeacher() == null || rating.getStudent() == null || rating.getCourse() == null) {
            throw new IllegalArgumentException("Teacher, Student and Course must not be null");
        }

        Integer teacherId = rating.getTeacher().getTid();
        Integer studentId = rating.getStudent().getSid();
        Integer courseId = rating.getCourse().getC_id();

        Teacher teacher = teacheRepository.findById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found with ID: " + teacherId));

        Student student = studentRepository.findBySid(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + studentId));

        Course course = rating.getCourse();

        // Validate student is in the same course
        if (student.getCourse() == null || student.getCourse().getC_id() == null || !student.getCourse().getC_id().equals(courseId)) {
            throw new IllegalArgumentException("Student is not enrolled in this course");
        }

        // Validate teacher teaches this course
        if (!teacherCourseRepository.existsByTeacher_TidAndCourse(teacherId, course)) {
            throw new IllegalArgumentException("Teacher does not teach this course");
        }

        // Upsert by (teacher, student, course)
        if (ratingTeacherRepository.existsByTeacher_TidAndStudent_SidAndCourse(teacherId, studentId, course)) {
            RatingTeacher existing = ratingTeacherRepository.findByTeacher_TidAndStudent_SidAndCourse(teacherId, studentId, course);
            return updateRating(existing.getId(), rating);
        }

        rating.setTeacher(teacher);
        rating.setStudent(student);
        rating.setCourse(course);
        return ratingTeacherRepository.save(rating);
    }

    public List<RatingTeacher> getAllRatings() {
        return ratingTeacherRepository.findAll();
    }

    public RatingTeacher getRatingById(Long id) {
        return ratingTeacherRepository.findById(id).orElseThrow(() -> new RuntimeException("Rating not found"));
    }

    public List<RatingTeacher> getRatingsByTeacherId(Long teacherId) {
        return ratingTeacherRepository.findByTeacherTid(teacherId);
    }

    public List<RatingTeacher> getRatingsByStudentId(Long studentId) {
        return ratingTeacherRepository.findByStudentSid(studentId);
    }

    public void deleteRating(Long id) {
        ratingTeacherRepository.deleteById(id);
    }
    public RatingTeacher updateRating(Long id, RatingTeacher rating) {
        RatingTeacher existing = getRatingById(id);
        existing.setScore(rating.getScore());
        return ratingTeacherRepository.save(existing);
    }
}
