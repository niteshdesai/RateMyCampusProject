package com.ratemycampus.controller;

import com.ratemycampus.entity.Department;
import com.ratemycampus.service.DepartmentService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

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
    public Department updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        return departmentService.updateDepartment(id, department);
    }

    @DeleteMapping("/{id}")
    public void deleteDepartment(@PathVariable Long id) {

        departmentService.deleteDepartment(id);
    }
}
