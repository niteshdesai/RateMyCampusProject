package com.ratemycampus.controller;


import com.ratemycampus.entity.Teacher;
import com.ratemycampus.service.TeacherService;
import com.ratemycampus.security.SecurityUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/teachers")

public class TeacherController {

    @Autowired private TeacherService teacherService;

    @Autowired private SecurityUtils securityUtils;

	@PostMapping(consumes = {"multipart/form-data"})
	public ResponseEntity<?> addTeacher(@Valid @RequestPart Teacher teacher,
										BindingResult result,
										@RequestPart(value = "image", required = false) MultipartFile image
	) throws IOException {
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
				if (currentUserDepartmentId == null || !currentUserDepartmentId.equals(teacher.getDepartment().getDeptId())) {
					HashMap<String, String> errors = new HashMap<>();
					errors.put("error", "You can only add teachers to your own department");
					return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
				}
			}
			
			if (image != null && !image.isEmpty()) {
				if (!isImageFile(image)) {
					HashMap<String, String> errors = new HashMap<>();
					errors.put("image", "File must be an image (jpg, jpeg, png)");
					return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
				}
				String imagePath = saveImage(image);
				teacher.setTimg(imagePath);
			} else {
				HashMap<String, String> errors = new HashMap<>();
				errors.put("image", "Image Is Required");
				return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
			}
			Teacher saved = teacherService.addTeacher(teacher);
			return new ResponseEntity<>(saved, HttpStatus.CREATED);
		} catch (EntityNotFoundException e) {
			HashMap<String, String> errors = new HashMap<>();
			errors.put("error", e.getMessage());
			return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
		}
	}

    @GetMapping
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        return ResponseEntity.ok(teacherService.getAllTeachers());
    }

    @GetMapping("/college/{collegeId}")
    public ResponseEntity<List<Teacher>> getTeachersByCollege(@PathVariable Long collegeId) {
        return ResponseEntity.ok(teacherService.getTeachersByCollege(collegeId));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<Teacher>> getTeachersByDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(teacherService.getTeachersByDepartment(departmentId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable Integer id) {
        return ResponseEntity.ok(teacherService.getTeacherById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTeacher(@Valid @RequestBody Teacher updated,BindingResult result,  @PathVariable Integer id) {
    	try {
    		Teacher teacher = teacherService.getTeacherById(id);
    		
    		// Validate department ownership for HOD role
    		String currentUserRole = securityUtils.getCurrentUserRole();
    		if ("ROLE_HOD".equals(currentUserRole)) {
    		    Long currentUserDepartmentId = securityUtils.getCurrentUserDepartmentId();
    		    if (currentUserDepartmentId == null || !currentUserDepartmentId.equals(teacher.getDepartment().getDeptId())) {
    		        HashMap<String, String> errors = new HashMap<>();
    		        errors.put("error", "You can only update teachers that belong to your department");
    		        return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
    		    }
    		    
    		    // Ensure the updated teacher also belongs to the same department
    		    if (!currentUserDepartmentId.equals(updated.getDepartment().getDeptId())) {
    		        HashMap<String, String> errors = new HashMap<>();
    		        errors.put("error", "You cannot change the department of a teacher");
    		        return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
    		    }
    		}
    		
    		updated.setTimg(teacher.getTimg());
    		
			if (result.hasErrors()) {
				Map<String, String> errors = new HashMap<>();
				result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
				return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
			}
			
			Teacher updatedData = teacherService.updateTeacher(id, updated);
			
			return ResponseEntity.ok(updatedData);
		} catch (Exception e) {
			HashMap<String, String> errors = new HashMap<>();
			errors.put("error", "Failed to Update teacher: " + e.getMessage());
			return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
		}
    }
	@PutMapping(value = "/updateTeacherImage/{Id}", consumes = { "multipart/form-data" })
	public ResponseEntity<?> updateTeacherImage(@PathVariable Integer Id,
											   @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
		try {
			Teacher teacher = teacherService.getTeacherById(Id);
			
			// Validate department ownership for HOD role
			String currentUserRole = securityUtils.getCurrentUserRole();
			if ("ROLE_HOD".equals(currentUserRole)) {
				Long currentUserDepartmentId = securityUtils.getCurrentUserDepartmentId();
				if (currentUserDepartmentId == null || !currentUserDepartmentId.equals(teacher.getDepartment().getDeptId())) {
					HashMap<String, String> errors = new HashMap<>();
					errors.put("error", "You can only update teacher images that belong to your department");
					return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
				}
			}
			
			String prevImage = teacher.getTimg();
			if (image != null && !image.isEmpty()) {
				if (!isImageFile(image)) {
					HashMap<String, String> errors = new HashMap<>();
					errors.put("image", "File must be an image (jpg, jpeg, png)");
					return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
				}
				// Delete old image if exists
				if (prevImage != null && !prevImage.isEmpty()) {
					File oldFile = new File("uploads/teacher-images/" + prevImage);
					if (oldFile.exists()) {
						oldFile.delete();
					}
				}
				String imagePath = saveImage(image);
				teacher.setTimg(imagePath);
			}
			Teacher updated = teacherService.updateTeacher(Id, teacher);
			return ResponseEntity.ok(updated);
		} catch (Exception e) {
			HashMap<String, String> errors = new HashMap<>();
			errors.put("error", "Failed to Update Teacher Image: " + e.getMessage());
			return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
		}
	}

    // New endpoints for managing teacher courses via TeacherCourse join table could be added
    // in a dedicated controller or here under /api/teachers/{teacherId}/courses
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTeacher(@PathVariable Integer id) {
        try {
            // Validate department ownership for HOD role
            String currentUserRole = securityUtils.getCurrentUserRole();
            if ("ROLE_HOD".equals(currentUserRole)) {
                Teacher existingTeacher = teacherService.getTeacherById(id);
                Long currentUserDepartmentId = securityUtils.getCurrentUserDepartmentId();
                if (currentUserDepartmentId == null || !currentUserDepartmentId.equals(existingTeacher.getDepartment().getDeptId())) {
                    HashMap<String, String> errors = new HashMap<>();
                    errors.put("error", "You can only delete teachers that belong to your department");
                    return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
                }
            }
            
            teacherService.deleteTeacher(id);
            return ResponseEntity.ok("Teacher deleted successfully");
        } catch (Exception e) {
            HashMap<String, String> errors = new HashMap<>();
            errors.put("error", e.getMessage());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
    }
    
    
    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (
            contentType.equals("image/jpeg") ||
            contentType.equals("image/png") ||
            contentType.equals("image/jpg")
        );
    }

    private String saveImage(MultipartFile image) throws IOException {
        String uploadDir = "uploads/teacher-images/";
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String originalFileName = image.getOriginalFilename();
        String fileExtension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        String fileName = java.util.UUID.randomUUID() + fileExtension;
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(image.getInputStream(), filePath);
        // Return relative path for storage in DB
        return uploadDir + fileName;
    }
}
