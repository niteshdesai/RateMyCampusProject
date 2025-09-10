package com.ratemycampus.controller;

import com.ratemycampus.entity.Course;
import com.ratemycampus.entity.Teacher;
import com.ratemycampus.entity.TeacherCourse;
import com.ratemycampus.repository.CourseRepository;
import com.ratemycampus.repository.TeacherCourseRepository;
import com.ratemycampus.repository.TeacherRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/api/teachers/{teacherId}/courses")
public class TeacherCourseController {

    @Autowired private TeacherRepository teacherRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private TeacherCourseRepository teacherCourseRepository;

    @GetMapping
    public ResponseEntity<?> list(@PathVariable Integer teacherId) {
        List<TeacherCourse> list = teacherCourseRepository.findByTeacher_Tid(teacherId);
        return ResponseEntity.ok(list);
    }

    // Teachers by course id
    @GetMapping("/all")
    public ResponseEntity<?> teachersByCourse(@PathVariable Integer teacherId, @RequestParam("courseId") Integer courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + courseId));
        return ResponseEntity.ok(teacherCourseRepository.findByCourse(course));
    }

    public static class CourseIdsRequest {
        public List<Integer> courseIds;
        public List<Integer> getCourseIds() { return courseIds; }
        public void setCourseIds(List<Integer> courseIds) { this.courseIds = courseIds; }
    }

    @PostMapping
    public ResponseEntity<?> assign(@PathVariable Integer teacherId, @RequestBody CourseIdsRequest body) {
        try {


            if (body == null || body.courseIds == null || body.courseIds.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "courseIds is required"));
            }

            Teacher teacher = teacherRepository.findById(teacherId)
                    .orElseThrow(() -> new EntityNotFoundException("Teacher not found with ID: " + teacherId));

            List<TeacherCourse> created = new ArrayList<>();
            for (Integer courseId : body.courseIds) {
                Course course = courseRepository.findById(courseId)
                        .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + courseId));

                if (!teacherCourseRepository.existsByTeacher_TidAndCourse(teacherId, course)) {
                    TeacherCourse tc = new TeacherCourse(teacher, course);
                    created.add(teacherCourseRepository.save(tc));
                }
                else
                {
                    HashMap<String, String> errors = new HashMap<>();
                    errors.put("error",  "Teacher ID:"+teacherId+" Course Id:"+courseId+" Already Available");
                    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
                }
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        }catch (EntityNotFoundException e)
        {
            HashMap<String, String> errors = new HashMap<>();
            errors.put("error",  e.getMessage());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> unassign(@PathVariable Integer teacherId, @RequestBody CourseIdsRequest body) {
        if (body == null || body.courseIds == null || body.courseIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "courseIds is required"));
        }

        for (Integer courseId : body.courseIds) {
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + courseId));
            teacherCourseRepository.deleteByTeacher_TidAndCourse(teacherId, course);
        }

        return ResponseEntity.ok(Map.of("message", "Unassigned successfully"));
    }
}


