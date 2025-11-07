package com.ratemycampus.service;

import com.ratemycampus.dto.TeacherCourseDTO;
import com.ratemycampus.dto.DtoMapper;
import com.ratemycampus.entity.Course;
import com.ratemycampus.entity.Teacher;
import com.ratemycampus.entity.TeacherCourse;
import com.ratemycampus.repository.CourseRepository;
import com.ratemycampus.repository.TeacherCourseRepository;
import com.ratemycampus.repository.TeacherRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeacherCourseService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherCourseRepository teacherCourseRepository;

    public List<TeacherCourse> listRaw(Integer teacherId) {
        return teacherCourseRepository.findByTeacher_Tid(teacherId);
    }

    public List<TeacherCourseDTO> list(Integer teacherId) {
        List<TeacherCourse> list = teacherCourseRepository.findByTeacher_Tid(teacherId);
        return list.stream().map(DtoMapper::toTeacherCourseDTO).toList();
      

    }

    public List<TeacherCourseDTO> teachersByCourse(Integer courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + courseId));
        List<TeacherCourse> teacherCourses = teacherCourseRepository.findByCourse(course);
        return teacherCourses.stream().map(DtoMapper::toTeacherCourseDTO).toList();
    }

    public List<TeacherCourseDTO> assign(Integer teacherId, List<Integer> courseIds) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found with ID: " + teacherId));

        List<TeacherCourse> created = new ArrayList<>();
        for (Integer courseId : courseIds) {
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + courseId));

            if (!teacherCourseRepository.existsByTeacher_TidAndCourse(teacherId, course)) {
                TeacherCourse tc = new TeacherCourse(teacher, course);
                TeacherCourse savedTc = teacherCourseRepository.save(tc);
                created.add(savedTc);
            } else {
                throw new IllegalArgumentException("Teacher ID:" + teacherId + " Course Id:" + courseId + " Already Available");
            }
        }

        return created.stream().map(DtoMapper::toTeacherCourseDTO).toList();
    }

    public void unassign(Integer teacherId, Integer courseId) {
        // Verify the course exists
        courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + courseId));

        // Verify the assignment exists
        TeacherCourse teacherCourse = teacherCourseRepository.findByTeacher_TidAndCourse(teacherId, 
            courseRepository.getReferenceById(courseId));
        if (teacherCourse == null) {
            throw new EntityNotFoundException("Teacher-Course assignment not found for Teacher ID: " + teacherId + " and Course ID: " + courseId);
        }

        teacherCourseRepository.deleteTeacherCourseAssignment(teacherId, courseId);
    }
}
