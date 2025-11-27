package com.ratemycampus.controller;

import com.ratemycampus.entity.Department;
import com.ratemycampus.dto.DepartmentDTO;
import com.ratemycampus.dto.DtoMapper;
import com.ratemycampus.service.DepartmentService;
import com.ratemycampus.security.SecurityUtils;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private SecurityUtils securityUtils;

    @PostMapping
    public ResponseEntity<?> addDepartment(@Valid @RequestBody Department department,BindingResult result) {
    	
    	try {
    	if (result.hasErrors()) {
	        HashMap<String, String> errors = new HashMap<>();
	        result.getFieldErrors().forEach(error -> {
	            errors.put(error.getField(), error.getDefaultMessage());
	
	        });
	        
	        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	 }


	 // Validate college ownership
	 Long currentUserCollegeId = securityUtils.getCurrentUserCollegeId();
	 if (currentUserCollegeId == null || !currentUserCollegeId.equals(department.getCollege().getCid())) {
	     HashMap<String, String> errors = new HashMap<>();
	     errors.put("error", "You can only create departments for your own college");
	     return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
	 }
	 
    	Department saveDepartment=departmentService.saveDepartment(department);
    	return new ResponseEntity<>(saveDepartment, HttpStatus.CREATED);
    	} catch (Exception e) {
    		HashMap<String, String> errors = new HashMap<>();
            errors.put("error", e.getMessage());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
		}
    }

    @GetMapping
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/college/{collegeId}")
    public List<Department> getDepartmentsByCollege(@PathVariable Long collegeId) {
        return departmentService.getDepartmentsByCollegeId(collegeId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDepartmentById(@PathVariable Long id) {

        try
        {
            Department department=departmentService.getDepartmentById(id);
            return ResponseEntity.ok(department);
        } catch (RuntimeException e) {
            HashMap<String, String> errors = new HashMap<>();
            errors.put("error", e.getMessage());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        try {
            // Validate college ownership
            Long currentUserCollegeId = securityUtils.getCurrentUserCollegeId();
            Department existingDepartment = departmentService.getDepartmentById(id);
            
            if (currentUserCollegeId == null || !currentUserCollegeId.equals(existingDepartment.getCollege().getCid())) {
                HashMap<String, String> errors = new HashMap<>();
                errors.put("error", "You can only update departments that belong to your college");
                return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
            }
            
            // Ensure the updated department also belongs to the same college
            if (!currentUserCollegeId.equals(department.getCollege().getCid())) {
                HashMap<String, String> errors = new HashMap<>();
                errors.put("error", "You cannot change the college of a department");
                return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
            }
            
            Department updatedDepartment = departmentService.updateDepartment(id, department);
            return ResponseEntity.ok(updatedDepartment);
        } catch (Exception e) {
            HashMap<String, String> errors = new HashMap<>();
            errors.put("error", e.getMessage());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDepartment(@PathVariable Long id) {
        try {
            // Validate college ownership
            Long currentUserCollegeId = securityUtils.getCurrentUserCollegeId();
            Department existingDepartment = departmentService.getDepartmentById(id);
            
            if (currentUserCollegeId == null || !currentUserCollegeId.equals(existingDepartment.getCollege().getCid())) {
                HashMap<String, String> errors = new HashMap<>();
                errors.put("error", "You can only delete departments that belong to your college");
                return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
            }
            
            departmentService.deleteDepartment(id);
            return ResponseEntity.ok("Department deleted successfully");
        } catch (Exception e) {
            HashMap<String, String> errors = new HashMap<>();
            errors.put("error", e.getMessage());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/{id}/students/count")
    public ResponseEntity<Long> countStudentsByDepartment(@PathVariable Long id) {
        try {
            long count = departmentService.countStudentsByDepartmentId(id);
            return ResponseEntity.ok(count);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
        @GetMapping("/{id}/teachers/count")
    public ResponseEntity<Long> countTeachersByDepartment(@PathVariable Long id) {
        try {
            long count = departmentService.countTeachersByDepartmentId(id);
            return ResponseEntity.ok(count);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{id}/courses/count")
    public ResponseEntity<Long> countCoursesByDepartment(@PathVariable Long id) {
        try {
            long count = departmentService.countCoursesByDepartmentId(id);
            return ResponseEntity.ok(count);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
