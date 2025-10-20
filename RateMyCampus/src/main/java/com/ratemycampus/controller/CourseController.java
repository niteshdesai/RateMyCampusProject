package com.ratemycampus.controller;

import com.ratemycampus.entity.Course;
import com.ratemycampus.dto.CourseDTO;
import com.ratemycampus.dto.DtoMapper;
import com.ratemycampus.exception.ResourceNotFoundException;
import com.ratemycampus.service.CourseService;
import com.ratemycampus.security.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private SecurityUtils securityUtils;

    @PostMapping
    public ResponseEntity<?> createCourse(@Valid @RequestBody Course course,BindingResult result) {
    	 try {
         	
     		if (result.hasErrors()) {
     	        HashMap<String, String> errors = new HashMap<>();
     	        result.getFieldErrors().forEach(error -> {
     	            errors.put(error.getField(), error.getDefaultMessage());
     	
     	        });
     	
     	        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
     	 }
     	 
     	 // Validate department ownership for HOD role
     	 String currentUserRole = securityUtils.getCurrentUserRole();
     	 if ("ROLE_HOD".equals(currentUserRole)) {
     	     Long currentUserDepartmentId = securityUtils.getCurrentUserDepartmentId();
     	     if (currentUserDepartmentId == null || !currentUserDepartmentId.equals(course.getDepartment().getDeptId())) {
     	         HashMap<String, String> errors = new HashMap<>();
     	         errors.put("error", "You can only create courses for your own department");
     	         return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
     	     }
     	 }
     	
         Course saved = courseService.addCourse(course);
         return new ResponseEntity<>(saved, HttpStatus.CREATED);
     } catch (ResourceNotFoundException e) {
     	HashMap<String, String> errors = new HashMap<>();
         errors.put("error", e.getMessage());
         return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
     }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Integer id, @Valid @RequestBody Course course) {
        try {
            // Validate department ownership for HOD role
            String currentUserRole = securityUtils.getCurrentUserRole();
            if ("ROLE_HOD".equals(currentUserRole)) {
                Course existingCourse = courseService.getCourseById(id);
                Long currentUserDepartmentId = securityUtils.getCurrentUserDepartmentId();
                if (currentUserDepartmentId == null || !currentUserDepartmentId.equals(existingCourse.getDepartment().getDeptId())) {
                    HashMap<String, String> errors = new HashMap<>();
                    errors.put("error", "You can only update courses that belong to your department");
                    return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
                }
                
                // Ensure the updated course also belongs to the same department
                if (!currentUserDepartmentId.equals(course.getDepartment().getDeptId())) {
                    HashMap<String, String> errors = new HashMap<>();
                    errors.put("error", "You cannot change the department of a course");
                    return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
                }
            }
            
            Course updatedCourse = courseService.updateCourse(id, course);
            return ResponseEntity.ok(updatedCourse);
        } catch (ResourceNotFoundException e) {
            Map<String, String> errors = new HashMap<>();
            errors.put("error", e.getMessage());
            return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Integer id) {
        try {
            // Validate department ownership for HOD role
            String currentUserRole = securityUtils.getCurrentUserRole();
            if ("ROLE_HOD".equals(currentUserRole)) {
                Course existingCourse = courseService.getCourseById(id);
                Long currentUserDepartmentId = securityUtils.getCurrentUserDepartmentId();
                if (currentUserDepartmentId == null || !currentUserDepartmentId.equals(existingCourse.getDepartment().getDeptId())) {
                    HashMap<String, String> errors = new HashMap<>();
                    errors.put("error", "You can only delete courses that belong to your department");
                    return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
                }
            }
            
            courseService.deleteCourse(id);
            return ResponseEntity.ok("Course deleted successfully");
        } catch (ResourceNotFoundException e) {
            Map<String, String> errors = new HashMap<>();
            errors.put("error", e.getMessage());
            return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable Integer id) {
        try {
            Course course = courseService.getCourseById(id);
            CourseDTO courseDTO = DtoMapper.toCourseDTO(course);
            return ResponseEntity.ok(courseDTO);
        } catch (ResourceNotFoundException e) {
            Map<String, String> errors = new HashMap<>();
            errors.put("error", e.getMessage());
            return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllCourses() {
        try {
            List<Course> courses = courseService.getAllCourses();
            List<CourseDTO> courseDTOs = courses.stream()
                    .map(DtoMapper::toCourseDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(courseDTOs);
        } catch (ResourceNotFoundException e) {
            Map<String, String> errors = new HashMap<>();
            errors.put("error", e.getMessage());
            return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<?> getCoursesByDepartmentId(@PathVariable Long departmentId) {
        List<Course> courses = courseService.getCoursesByDepartmentId(departmentId);
        List<CourseDTO> courseDTOs = courses.stream()
                .map(DtoMapper::toCourseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(courseDTOs);
    }
}
