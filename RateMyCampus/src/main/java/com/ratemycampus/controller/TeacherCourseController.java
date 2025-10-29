package com.ratemycampus.controller;

import com.ratemycampus.dto.DtoMapper;
import com.ratemycampus.dto.TeacherCourseDTO;
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

    @Autowired private com.ratemycampus.service.TeacherCourseService teacherCourseService;

    @GetMapping
    public ResponseEntity<?> list(@PathVariable Integer teacherId) {
        return ResponseEntity.ok(teacherCourseService.list(teacherId));
    }

    // Teachers by course id
    @GetMapping("/all")
    public ResponseEntity<?> teachersByCourse(@PathVariable Integer teacherId, @RequestParam("courseId") Integer courseId) {
        return ResponseEntity.ok(teacherCourseService.teachersByCourse(courseId));
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

            return ResponseEntity.status(HttpStatus.CREATED).body(teacherCourseService.assign(teacherId, body.courseIds));
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            HashMap<String, String> errors = new HashMap<>();
            errors.put("error", e.getMessage());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> unassign(@PathVariable Integer teacherId, @RequestBody CourseIdsRequest body) {
        if (body == null || body.courseIds == null || body.courseIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "courseIds is required"));
        }

        try {
            teacherCourseService.unassign(teacherId, body.courseIds);
            return ResponseEntity.ok(Map.of("message", "Unassigned successfully"));
        } catch (EntityNotFoundException e) {
            HashMap<String, String> errors = new HashMap<>();
            errors.put("error", e.getMessage());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
    }
}


